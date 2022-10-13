package uboat;

//taken from: http://www.servletworld.com/servlet-tutorials/servlet3/multipartconfig-file-upload-example.html
// and http://docs.oracle.com/javaee/6/tutorial/doc/glraq.html

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
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
import java.util.Collection;
import java.util.Scanner;

import static general.ConstantsHTTP.UBOAT_CONTEXT;
import static general.ConstantsHTTP.UPLOAD_FILE;

@WebServlet(UBOAT_CONTEXT+UPLOAD_FILE)
@MultipartConfig(fileSizeThreshold = 1024 * 1024, maxFileSize = 1024 * 1024 * 5, maxRequestSize = 1024 * 1024 * 5 * 5)
public class FileUploadServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/plain");
        PrintWriter out = response.getWriter();

        Collection<Part> parts = request.getParts();

        out.println("Total file : " + parts.size());
        if(parts.size()!=1) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().println("ERROR! must upload only 1 XML file.");
        }
        String username = SessionUtils.getUsername(request);

        if (username == null||!ServletUtils.getUboatManager().isUboatExist(username))
        {
            if(username == null)
                response.getWriter().println("Must login as UBOAT first!");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        Part input=parts.stream().findFirst().orElse(null);
        if(input!=null)
        {
            try {
                ServletUtils.getUboatManager()
                        .getBattleFieldController(username)
                        .assignXMLFileToUboat(
                                readFromInputStream(input.getInputStream()));
                response.setStatus(HttpServletResponse.SC_OK);
                out.println("Success upload '" + input.getSubmittedFileName() + "' to server for uboat user:" + username);
            }
            catch (RuntimeException e) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().println(e.getMessage());
            }
        }
        else
            response.setStatus(HttpServletResponse.SC_BAD_GATEWAY);
    }

    private String readFromInputStream(InputStream inputStream) {
        return new Scanner(inputStream).useDelimiter("\\Z").next();
    }

}
