package uboat;

//taken from: http://www.servletworld.com/servlet-tutorials/servlet3/multipartconfig-file-upload-example.html
// and http://docs.oracle.com/javaee/6/tutorial/doc/glraq.html

import UBoatDTO.GameStatus;
import enigmaEngine.Engine;
import enigmaEngine.EnigmaEngine;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletOutputStream;
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
        ServletOutputStream out = response.getOutputStream();

        Collection<Part> parts = request.getParts();

        if(parts.size()!=1) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            out.println("ERROR! must upload only 1 XML file.");
            out.flush();
        }
        String username = SessionUtils.getUsername(request);

        if (username == null||!ServletUtils.getSystemManager().isUboatExist(username))
        {

                out.println("Must login as UBOAT first!");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }
        ServletUtils.logRequestAndTime(username,"FileUploadServlet");
        try{
        Part input=parts.stream().findFirst().orElse(null);
        if(input!=null)
        {
            try {
                SingleBattleFieldController uboatController= ServletUtils.getSystemManager().getBattleFieldController(username);
                GameStatus gameStatus=uboatController.getGameStatus();
                ContestDataManager contestDataManager = uboatController.getContestDataManager();
                if(contestDataManager!=null)
                {
                    ServletUtils.getSystemManager().removeBattlefield(contestDataManager.getBattlefieldName());
                }
                if(gameStatus==GameStatus.WAITING_FOR_ALLIES||gameStatus==GameStatus.ACTIVE)
                {
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    out.print("cannot change Enigma machine after pressing ready or during active contest.");
                    out.flush();
                    return;
                }
                    Engine engine=new EnigmaEngine();
                    String xmlContent=readFromInputStream(input.getInputStream());
                    engine.loadXMLFileFromStringContent(xmlContent);
                    String battleFieldName=engine.getBattlefieldDataDTO().getBattlefieldName();
                    synchronized (getServletContext()) {

                        if (ServletUtils.getSystemManager().ifBattleFieldExist(battleFieldName)) {
                            //    ServletUtils.getSystemManager().removeUserName(username,ApplicationType.UBOAT);

                             out.println("Battlefield " + battleFieldName + " already exists. Please choose a different battlefield.");
                            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                           // out.flush();
                            return;
                        }
                        ServletUtils.getSystemManager().addNewBattleField(battleFieldName);

                    }
                    uboatController.assignXMLFileToUboat(xmlContent,engine);
                    String machineDataContent = ServletUtils.getGson().toJson(engine.getMachineData());
                    out.println(machineDataContent);
                   // out.flush();
                    response.setStatus(HttpServletResponse.SC_OK);

                System.out.println("Success upload '" + input.getSubmittedFileName() + "' to server for uboat user:" + username);
            }
            catch (RuntimeException e) {
                System.out.println("file upload error:"+e.getMessage());
               ServletUtils.setBadRequestErrorResponse(e,response);
            }
        }
        else
            response.setStatus(HttpServletResponse.SC_BAD_GATEWAY);
        }catch (RuntimeException e) {
            ServletUtils.setBadRequestErrorResponse(e,response);
        }
    }

    private String readFromInputStream(InputStream inputStream) {
        return new Scanner(inputStream).useDelimiter("\\Z").next();
    }

}
