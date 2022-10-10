package uboat.fileupload;

//taken from: http://www.servletworld.com/servlet-tutorials/servlet3/multipartconfig-file-upload-example.html
// and http://docs.oracle.com/javaee/6/tutorial/doc/glraq.html

import constants.Constants;
import general.ApplicationType;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;
import utils.ServletUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.Collection;
import java.util.Objects;
import java.util.Scanner;

import static constants.Constants.USERNAME;

@WebServlet("/uboat/upload-file")
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


        String usernameFromParameter = request.getParameter(USERNAME);
        if (usernameFromParameter == null || usernameFromParameter.isEmpty()) {
            //no username in session and no username in parameter - not standard situation. it's a conflict

            // stands for conflict in server state
            response.setStatus(HttpServletResponse.SC_CONFLICT);
        } else {
            //normalize the username value
            usernameFromParameter = usernameFromParameter.trim();
            synchronized (this) {
                if (!ServletUtils.getUboatManager(getServletContext()).isUboatExist(usernameFromParameter)) {
                    String errorMessage = "Uboat username " + usernameFromParameter + " not exists. Please enter a different username.";

                    // stands for unauthorized as there is already such user with this name
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    response.getOutputStream().print(errorMessage);
                }
                else {
                    Part input=parts.stream().findFirst().orElse(null);
                    if(input!=null)
                    {
                    ServletUtils.getUboatManager(getServletContext()).assignXMLFileToUboat(usernameFromParameter, readFromInputStream(input.getInputStream()));
                    response.setStatus(HttpServletResponse.SC_OK);
                    out.println("Success upload '"+input.getSubmittedFileName()+"' to server for uboat user:"  + usernameFromParameter);
                    }
                    else
                        response.setStatus(HttpServletResponse.SC_BAD_GATEWAY);}

                }
            }
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
