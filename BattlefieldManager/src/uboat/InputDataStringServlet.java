package uboat;

//taken from: http://www.servletworld.com/servlet-tutorials/servlet3/multipartconfig-file-upload-example.html
// and http://docs.oracle.com/javaee/6/tutorial/doc/glraq.html

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
import java.util.Properties;
import java.util.Scanner;

import static general.ConstantsHTTP.INPUT_STRING;
import static general.ConstantsHTTP.UBOAT_CONTEXT;


@WebServlet(name = "InputDataStringServlet", urlPatterns = {UBOAT_CONTEXT+INPUT_STRING})
public class InputDataStringServlet extends HttpServlet {



    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/plain");
        PrintWriter out = response.getWriter();


        String username = SessionUtils.getUsername(request);

        if (username == null||!ServletUtils.getUboatManager().isUboatExist(username))
        {
            if(username == null)
                response.getWriter().println("Must login as UBOAT first!");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

            Properties prop = new Properties();
            prop.load(request.getInputStream());

            String inputString = prop.getProperty("input");

        if(inputString!=null)
        {
       String output= ServletUtils.getUboatManager()
                .getBattleFieldController(username)
               .getEnigmaEngine()
               .processDataInput(inputString);

        response.setStatus(HttpServletResponse.SC_OK);
        out.println("output="+output);
        }
        else
            response.setStatus(HttpServletResponse.SC_BAD_GATEWAY);
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
