package Ally;

//taken from: http://www.servletworld.com/servlet-tutorials/servlet3/multipartconfig-file-upload-example.html
// and http://docs.oracle.com/javaee/6/tutorial/doc/glraq.html

import allyDTOs.AllyContestDataAndTeams;
import allyDTOs.AllyDataDTO;
import allyDTOs.ContestDataDTO;
import com.google.gson.Gson;
import constants.Constants;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;
import uboat.SingleBattleFieldController;
import utils.ServletUtils;
import utils.SessionUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

import static general.ConstantsHTTP.UBOAT_NAME;


@WebServlet(name = "UpdateContestTeamsAndDataServlet", urlPatterns = {"/ally/update-contest-data"})
public class UpdateContestTeamsAndDataServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/plain");
        PrintWriter out = response.getWriter();

        String username = SessionUtils.getUsername(request);
        String uboatName = request.getParameter(UBOAT_NAME);
        SingleAllyController singleAllyController= ServletUtils.getSystemManager().getSingleAlly(username);
        singleAllyController.assignAllyToUboat(uboatName);

        if (username == null||!ServletUtils.getSystemManager().isAllyExist(username))
        {
            if(username == null)
                response.getWriter().println("Must login as Ally first!");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        try {
            Gson gson = ServletUtils.getGson();
            String uboatManager=ServletUtils.getSystemManager().getSingleAllyController(username).getUboatNameManager();
            SingleBattleFieldController uboatController=ServletUtils.getSystemManager()
                    .getBattleFieldController(uboatManager);
            List<AllyDataDTO> otherAllyDataDTOList= uboatController
                    .getAlliesDataListForUboat()
                    .stream()
                    .filter(AllyData->
                            !AllyData.getAllyName().equals(username))
                    .collect(Collectors.toList());
            ContestDataDTO contestDataDTO=uboatController.getContestDataDTO();
            out.println(
                    gson.toJson(
                            new AllyContestDataAndTeams(otherAllyDataDTOList,contestDataDTO)
                    ));
            out.flush();
            response.setStatus(HttpServletResponse.SC_OK);
        }catch (RuntimeException e) {
            response.setContentType("text/plain");
            response.getWriter().println(e.getMessage());
            response.getWriter().flush();
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }

    }






    private void printPart(Part part, PrintWriter out) {
        StringBuilder sb = new StringBuilder();
        sb
            .append("Parameter Name: ").append(part.getName()).append("\n")
            .append("Content Type (of the file): ").append(part.getContentType()).append("\n")
            .append("Size (of the file): ").append(part.getSize()).append("\n")
            .append("Part Headers:").append("\n");

        for (String header : part.getHeaderNames()) {
            sb.append(header).append(" : ").append(part.getHeader(header)).append("\n");
        }

        out.println(sb.toString());
    }

    private String readFromInputStream(InputStream inputStream) {
        return new Scanner(inputStream).useDelimiter("\\Z").next();
    }

    private void printFileContent(String content, PrintWriter out) {
        out.println("------------------------------------------------------------File content:-----");
        out.println(content);
    }
}
