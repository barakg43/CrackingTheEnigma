package ally;

//taken from: http://www.servletworld.com/servlet-tutorials/servlet3/multipartconfig-file-upload-example.html
// and http://docs.oracle.com/javaee/6/tutorial/doc/glraq.html

import agent.AgentDataDTO;
import allyDTOs.AllyContestScreenDTO;
import allyDTOs.ContestDataDTO;
import com.google.gson.Gson;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;
import utils.ServletUtils;
import utils.SessionUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.List;
import java.util.Scanner;


@WebServlet(name = "UpdateDashboardScreenAllyServlet", urlPatterns = {"/ally/update-dashboard-data"})
public class UpdateDashboardScreenAllyServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/plain");
        PrintWriter out = response.getWriter();


        String username = SessionUtils.getUsername(request);

        if (username == null||!ServletUtils.getAlliesManager().isUserExist(username))
        {
            if(username == null)
                response.getWriter().println("Must login as AGENT first!");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        try {
            Gson gson = ServletUtils.getGson();
            List<ContestDataDTO> contestDataDTOList= ServletUtils.getUboatManager().getAllContestDataList();
            List<AgentDataDTO> agentDataDTOList= ServletUtils.getAlliesManager()
                    .getSingleAllyController(username)
                    .getAgentDataListForAlly();


            out.println(
                    gson.toJson(
                            new AllyContestScreenDTO(agentDataDTOList,contestDataDTOList)
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
