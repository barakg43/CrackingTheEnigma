package uboat;


import com.google.gson.JsonObject;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import utils.ServletUtils;
import utils.SessionUtils;

import java.io.IOException;

import static general.ConstantsHTTP.*;


@WebServlet(name = "InputDataStringServlet", urlPatterns = {UBOAT_CONTEXT+INPUT_STRING})
public class InputDataStringServlet extends HttpServlet {




    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/plain");
        ServletOutputStream out = response.getOutputStream();


        String username = SessionUtils.getUsername(request);

        if (username == null||!ServletUtils.getSystemManager().isUboatExist(username))
        {
            if(username == null)
                out.println("Must login as UBOAT first!");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }
        ServletUtils.logRequestAndTime(username,"InputDataStringServlet");
        try{
        String inputString= ServletUtils.getGson().fromJson(request.getReader(), JsonObject.class).get(INPUT_PROPERTY).getAsString();


        if(inputString!=null)
        {
       SingleBattleFieldController uboatController= ServletUtils.getSystemManager()
                    .getBattleFieldController(username);
       String output=uboatController
               .getEnigmaEngine()
               .processDataInput(inputString);
       uboatController.setContestInitConfiguration(output);
       out.println(String.format(SINGLE_JSON_FORMAT,OUTPUT_PROPERTY,output));
        response.setStatus(HttpServletResponse.SC_OK);

        }
        else
            response.setStatus(HttpServletResponse.SC_BAD_GATEWAY);
        }catch (RuntimeException e) {
            ServletUtils.setBadRequestErrorResponse(e,response);
        }
    }







}
