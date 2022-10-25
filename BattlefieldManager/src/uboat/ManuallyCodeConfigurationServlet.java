package uboat;

import com.google.gson.Gson;
import engineDTOs.CodeFormatDTO;
import enigmaEngine.Engine;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import utils.ServletUtils;
import utils.SessionUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;

import static general.ConstantsHTTP.*;


@WebServlet(name = "ManuallyCodeConfigurationServlet", urlPatterns = {UBOAT_CONTEXT+MANUALLY_CODE})
public class ManuallyCodeConfigurationServlet extends HttpServlet {


    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {

        String username = SessionUtils.getUsername(request);

        if (username == null||!ServletUtils.getSystemManager().isUboatExist(username))
        {
            if(username == null)
                response.getWriter().println("Must login as UBOAT first!");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }
        ServletUtils.logRequestAndTime(username,"ManuallyCodeConfigurationServlet");
        try{
        Engine enigmaEngine=ServletUtils.getSystemManager()
                .getBattleFieldController(username)
                .getEnigmaEngine();
            Reader inputReader = new BufferedReader(new InputStreamReader(request.getInputStream()));
            Gson gson = ServletUtils.getGson();
            CodeFormatDTO codeFormatDTO = gson.fromJson(inputReader, CodeFormatDTO.class);
            inputReader.close();
            System.out.println(codeFormatDTO);
            enigmaEngine.setCodeManually(codeFormatDTO);
            getServletContext().getRequestDispatcher(UBOAT_CONTEXT+ALL_CODE).forward(request, response);
        }catch (RuntimeException | ServletException e) {
            ServletUtils.setBadRequestErrorResponse(e,response);
        }



    }
}


