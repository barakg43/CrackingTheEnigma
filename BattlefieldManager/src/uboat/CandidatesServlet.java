package uboat;

import allyDTOs.AllyCandidateDTO;
import allyDTOs.AllyDataDTO;
import com.google.gson.Gson;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import utils.ServletUtils;
import utils.SessionUtils;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;


@WebServlet(name = "CandidatesServlet", urlPatterns = {"/uboat/candidates"})
public class CandidatesServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

        String username = SessionUtils.getUsername(request);

        if (username == null||!ServletUtils.getUboatManager().isUboatExist(username))
        {
            if(username == null)
                response.getWriter().println("Must login as UBOAT first!");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        List<AllyCandidateDTO> allyCandidateDTOList = new ArrayList<>();
        List<AllyDataDTO> allyDataDTOSet = ServletUtils.getUboatManager()
                .getBattleFieldController(username)
                .getAlliesDataListForUboat();
        for (AllyDataDTO allyData:allyDataDTOSet) {
            allyCandidateDTOList.addAll(ServletUtils.getAlliesManager()
                    .getSingleAllyController(allyData.getAllyName())
                    .getAllyCandidateDTOListWithVersion());

        }

        //returning JSON objects, not HTML
        response.setContentType("application/json");
        try (PrintWriter out = response.getWriter()) {
            Gson gson = ServletUtils.getGson();
            String json = gson.toJson(allyCandidateDTOList);
            out.println(json);
            out.flush();
        }
    }

}
