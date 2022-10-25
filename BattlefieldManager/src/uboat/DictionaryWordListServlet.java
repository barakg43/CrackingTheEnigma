package uboat;

import com.google.gson.Gson;
import enigmaEngine.Engine;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import utils.ServletUtils;
import utils.SessionUtils;

import java.io.IOException;
import java.io.PrintWriter;

import static general.ConstantsHTTP.DICTIONARY_WORDS;
import static general.ConstantsHTTP.UBOAT_CONTEXT;

@WebServlet(name = "DictionaryWordsServlet", urlPatterns = {UBOAT_CONTEXT+DICTIONARY_WORDS})
public class DictionaryWordListServlet extends HttpServlet {


    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

        String username = SessionUtils.getUsername(request);

        if (username == null||!ServletUtils.getSystemManager().isUboatExist(username))
        {
            if(username == null)
                response.getWriter().println("Must login as UBOAT first!");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }
        ServletUtils.logRequestAndTime(username,"DictionaryWordsServlet");
        try{
        Engine enigmaEngine=ServletUtils.getSystemManager().
                getBattleFieldController(username).
                getEnigmaEngine();

        //returning JSON objects, not HTML
        response.setContentType("application/json");
        try (PrintWriter out = response.getWriter()) {
            Gson gson = ServletUtils.getGson();
            String json = gson.toJson(enigmaEngine.getDictionary().getWordsSet());
            out.println(json);
            out.flush();
        }
        response.setStatus(HttpServletResponse.SC_OK);
        }catch (RuntimeException e) {
            ServletUtils.setBadRequestErrorResponse(e,response);
        }
    }

}
