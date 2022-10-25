package uboat;

import allyDTOs.AllyCandidateDTO;
import allyDTOs.AllyDataDTO;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import utils.ServletUtils;
import utils.SessionUtils;

import java.io.*;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static general.ConstantsHTTP.*;


@WebServlet(name = "CandidatesServlet", urlPatterns = {UBOAT_CONTEXT+UPDATE_CANDIDATES})
public class CandidatesServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

        String uboatName = SessionUtils.getUsername(request);

        if (uboatName == null||!ServletUtils.getSystemManager().isUboatExist(uboatName))
        {
            if(uboatName == null)
                response.getWriter().println("Must login as UBOAT first!");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }



        ServletUtils.logRequestAndTime(uboatName,"CandidatesServlet");
        List<AllyCandidateDTO> alliesCandidatesList = new ArrayList<>();
            try {
                SingleBattleFieldController uboatController=ServletUtils.getSystemManager()
                        .getBattleFieldController(uboatName);
                Gson gson = ServletUtils.getGson();
                Reader inputReader = new BufferedReader(new InputStreamReader(request.getInputStream()));
                Type mapType=new TypeToken<Map<String,Integer>>(){}.getType();
                Map<String,Integer> alliesVersionMap = gson.fromJson(inputReader, mapType);
                inputReader.close();
                List<AllyDataDTO> allyDataDTOSet = uboatController.getAlliesDataListForUboat();
                for (AllyDataDTO allyData:allyDataDTOSet) {
                    String allyName=allyData.getAllyName();
                    int version=alliesVersionMap.get(allyName);
                    alliesCandidatesList.addAll(ServletUtils.getSystemManager()
                            .getSingleAllyController(allyName)
                            .getAllyCandidateDTOListWithVersion(version));
                }
                //returning JSON objects, not HTML
                response.setContentType("application/json");
                try (PrintWriter out = response.getWriter()) {
                    String json = gson.toJson(alliesCandidatesList);
                    out.println(json);
                    out.flush();
                }
                response.setStatus(HttpServletResponse.SC_OK);
            }catch (RuntimeException e) {
                ServletUtils.setBadRequestErrorResponse(e,response);
            }




    }

}
