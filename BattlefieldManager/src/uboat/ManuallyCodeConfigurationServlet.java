package uboat;

import com.google.gson.Gson;
import engineDTOs.CodeFormatDTO;
import enigmaEngine.Engine;
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

import static general.ConstantsHTTP.MANUALLY_CODE;
import static general.ConstantsHTTP.UBOAT_CONTEXT;


@WebServlet(name = "ManuallyCodeConfigurationServlet", urlPatterns = {UBOAT_CONTEXT+MANUALLY_CODE})
public class ManuallyCodeConfigurationServlet extends HttpServlet {



    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws IOException {

        String username = SessionUtils.getUsername(request);

        if (username == null||!ServletUtils.getUboatManager().isUboatExist(username))
        {
            if(username == null)
                response.getWriter().println("Must login as UBOAT first!");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }
        Engine enigmaEngine=ServletUtils.getUboatManager()
                .getBattleFieldController(username)
                .getEnigmaEngine();
        try {
            Reader inputReader = new BufferedReader(new InputStreamReader(request.getInputStream()));
            Gson gson = ServletUtils.getGson();
            CodeFormatDTO codeFormatDTO = gson.fromJson(inputReader, CodeFormatDTO.class);
            System.out.println(codeFormatDTO);
            enigmaEngine.setCodeManually(codeFormatDTO);
            response.setStatus(HttpServletResponse.SC_OK);
        }catch (RuntimeException e) {
            response.setContentType("text/plain");
            response.getWriter().println(e.getMessage());
            response.getWriter().flush();
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }



        }
}


