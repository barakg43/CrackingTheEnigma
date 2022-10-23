package shared.servlet;


import agent.AgentDataDTO;
import com.google.gson.Gson;
import general.ApplicationType;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import systemManager.SystemManager;
import utils.ServletUtils;
import utils.SessionUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;

import static general.ConstantsHTTP.*;

@WebServlet(name = "LoginServlet", urlPatterns = {UBOAT_CONTEXT+LOGIN,ALLY_CONTEXT+LOGIN, AGENT_CONTEXT+LOGIN})
public class LoginServlet extends HttpServlet {

    // urls that starts with forward slash '/' are considered absolute
    // urls that doesn't start with forward slash '/' are considered relative to the place where this servlet request comes from
    // you can use absolute paths, but then you need to build them from scratch, starting from the context path
    // ( can be fetched from request.getContextPath() ) and then the 'absolute' path from it.
    // Each method with it's pros and cons...

     /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        String usernameFromSession = SessionUtils.getUsername(request);
        SystemManager userManager = ServletUtils.getSystemManager();
        if (usernameFromSession == null) {
            //user is not logged in yet
            String typeFromUrl = (request.getRequestURI().split("/")[2]).toUpperCase();
            try {
                String usernameFromParameter = request.getParameter(USERNAME);
                ApplicationType type = ApplicationType.valueOf(typeFromUrl);
                if (usernameFromParameter == null || usernameFromParameter.isEmpty()) {
                    response.getOutputStream().print("must use query parameter '"+USERNAME+"'");
                    response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                } else {
                    //normalize the username value
                    usernameFromParameter = usernameFromParameter.trim();
                    //typeFromUrl = typeFromUrl.trim().toUpperCase();

                synchronized (this) {
                        if (userManager.isUserExists(usernameFromParameter)) {
                            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                            response.getOutputStream().print("Username " + usernameFromParameter + " already exists. Please enter a different username.");
                            return;
                        }
                        //add the new user to the users list
                        try {
                            userManager.addUserName(usernameFromParameter, type);
                            try {
                                if (type == ApplicationType.AGENT)
                                    ServletUtils.getSystemManager().addNewAgentData(readAgentDTO(request));
                            } catch (RuntimeException ex) {
                                response.sendError(HttpServletResponse.SC_NO_CONTENT, "Error reading agent data from request.\n" + ex.getMessage());
                                return;
                            }

                        } catch (RuntimeException ex) {
                            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                            response.getOutputStream().print("Error: " + ex.getMessage());
                            return;
                        }
                        request.getSession(true).setAttribute(USERNAME, usernameFromParameter);
                        //redirect the request to the chat room - in order to actually change the URL
                        response.setStatus(HttpServletResponse.SC_OK);


                    }
                }
            }catch (RuntimeException | IOException ex) {
                            response.getOutputStream().print("Error: " + ex.getMessage());
                            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            }
        } else {
            //user is already logged in
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            if (userManager.isUserExists(usernameFromSession)) {
                System.out.println(usernameFromSession+" is exist");
                response.getOutputStream().print("Your logged in as " + usernameFromSession + " already.");
                response.getOutputStream().flush();
                }

        }

//            if (usernameFromParameter == null || usernameFromParameter.isEmpty()) {
//                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
//            } else {
//                //normalize the username value
//                usernameFromParameter = usernameFromParameter.trim();
//                //typeFromUrl = typeFromUrl.trim().toUpperCase();
//
//                synchronized (this) {
//                    if (userManager.isUserExists(usernameFromParameter)) {
//                        System.out.println("user is not exist");
//                            response.getOutputStream().print("Username " + usernameFromParameter + " already exists. Please enter a different username.");
//                            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
//                            return;
//                        }
//                        //add the new user to the users list
//                        //ApplicationType type= ApplicationType.valueOf(typeFromUrl);
//
//
//                        try {
//                            userManager.addUserName(usernameFromParameter,type);
//                            try {
//                                if(type==ApplicationType.AGENT)
//                                    ServletUtils.getSystemManager().addAgentData(readAgentDTO(request));
//                            }
//                                catch (RuntimeException ex) {
//                                response.sendError(HttpServletResponse.SC_NO_CONTENT, "Error reading agent data from request.\n"+ex.getMessage());
//                                return;
//                            }
//
//                        }catch (RuntimeException ex) {
//                            response.getOutputStream().print("Error: " + ex.getMessage());
//                            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
//                        }
//                        request.getSession(true).setAttribute(USERNAME, usernameFromParameter);
//                        //redirect the request to the chat room - in order to actually change the URL
//                        response.setStatus(HttpServletResponse.SC_OK);
//
//
//                    }
//            }
//        } else {
//            //user is already logged in
//            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
//        }
        }

    
    private AgentDataDTO readAgentDTO(HttpServletRequest request)
    {
        try {
            Reader inputReader = new BufferedReader(new InputStreamReader(request.getInputStream()));
            Gson gson = ServletUtils.getGson();

            AgentDataDTO agentDataDTO=gson.fromJson(inputReader, AgentDataDTO.class);
            System.out.println("agent name:" + agentDataDTO.getAgentName());
          //  System.out.println("ally name:" + gson.fromJson(inputReader, AgentDataDTO.class).getAllyTeamName());
            System.out.println("task size :" + agentDataDTO.getTasksSessionAmount());
            return agentDataDTO;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

// <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>
}
