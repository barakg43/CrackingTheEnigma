package uboat;

//taken from: http://www.servletworld.com/servlet-tutorials/servlet3/multipartconfig-file-upload-example.html
// and http://docs.oracle.com/javaee/6/tutorial/doc/glraq.html

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import utils.ServletUtils;
import utils.SessionUtils;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Properties;

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
            Properties prop = new Properties();
            prop.load(request.getInputStream());
            String inputString = prop.getProperty(INPUT_PROPERTY);

        if(inputString!=null)
        {
       String output= ServletUtils.getSystemManager()
                .getBattleFieldController(username)
               .getEnigmaEngine()
               .processDataInput(inputString);

       out.println(OUTPUT_PROPERTY+'='+output);
       out.flush();
        response.setStatus(HttpServletResponse.SC_OK);

        }
        else
            response.setStatus(HttpServletResponse.SC_BAD_GATEWAY);
    }







}
