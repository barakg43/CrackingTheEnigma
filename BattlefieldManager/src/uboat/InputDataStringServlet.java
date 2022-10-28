package uboat;


import com.google.gson.JsonObject;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import utils.ServletUtils;
import utils.SessionUtils;

import java.io.IOException;
import java.io.PrintWriter;

import static general.ConstantsHTTP.*;


@WebServlet(name = "InputDataStringServlet", urlPatterns = {UBOAT_CONTEXT+INPUT_STRING})
public class InputDataStringServlet extends HttpServlet {




    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/plain");
        PrintWriter out = response.getWriter();


        String username = SessionUtils.getUsername(request);

        if (username == null||!ServletUtils.getSystemManager().isUboatExist(username))
        {
            if(username == null)
                response.getWriter().println("Must login as UBOAT first!");
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
       out.format(SINGLE_JSON_FORMAT+"\r\n",OUTPUT_PROPERTY,output);
       out.flush();
        response.setStatus(HttpServletResponse.SC_OK);

        }
        else
            response.setStatus(HttpServletResponse.SC_BAD_GATEWAY);
        }catch (RuntimeException e) {
            ServletUtils.setBadRequestErrorResponse(e,response);
        }
    }







}
