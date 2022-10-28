package uboat;

import UBoatDTO.GameStatus;
import UBoatDTO.UboatCandidatesDTO;
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

import static general.ConstantsHTTP.UBOAT_CONTEXT;
import static general.ConstantsHTTP.UPDATE_CANDIDATES;


@WebServlet(name = "CandidatesServlet", urlPatterns = {UBOAT_CONTEXT+UPDATE_CANDIDATES})
public class AlliesCandidatesServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {

        String uboatName = SessionUtils.getUsername(request);
        PrintWriter out = response.getWriter();
        if (uboatName == null||!ServletUtils.getSystemManager().isUboatExist(uboatName))
        {
            if(uboatName == null)
                out.println("Must login as UBOAT first!");
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
                    System.out.println(allyName +" version before:"+ version);
                    List<AllyCandidateDTO> allyCandidateDTOList=ServletUtils.getSystemManager()
                            .getSingleAllyController(allyName)
                            .getAllyCandidateDTOListWithVersion(version);
                    alliesVersionMap.put(allyName,allyCandidateDTOList.size());
                    alliesCandidatesList.addAll(allyCandidateDTOList);
                }
                System.out.println("after update map:");
                for (String allyName:alliesVersionMap.keySet()) {
                    System.out.println(allyName +" version after:"+ alliesVersionMap.get(allyName));
                }
                GameStatus gameStatus=uboatController.getContestDataDTO().getGameStatus();
                //returning JSON objects, not HTML
                response.setContentType("application/json");

                    String json = gson.toJson(new UboatCandidatesDTO(alliesCandidatesList,gameStatus,alliesVersionMap));
                    out.println(json);
                    out.flush();

                response.setStatus(HttpServletResponse.SC_OK);
            }catch (RuntimeException e) {
                ServletUtils.setBadRequestErrorResponse(e,response);
            }




    }

}
