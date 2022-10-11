package shared.servlet;


import constants.Constants;
import general.ApplicationType;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import users.UserManager;
import utils.ServletUtils;
import utils.SessionUtils;

import java.io.IOException;

import static constants.Constants.TYPE;
import static constants.Constants.USERNAME;

@WebServlet(name = "LoginServlet", urlPatterns = {"/login"})
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
        UserManager userManager = ServletUtils.getSystemUserManager(getServletContext());
        if (usernameFromSession == null) {
            //user is not logged in yet
            String usernameFromParameter = request.getParameter(USERNAME);
            String typeUserNameFromParameter= request.getParameter(TYPE);
            if (usernameFromParameter == null || usernameFromParameter.isEmpty()||typeUserNameFromParameter == null || typeUserNameFromParameter.isEmpty()) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

            } else {
                //normalize the username value
                usernameFromParameter = usernameFromParameter.trim();
                typeUserNameFromParameter = typeUserNameFromParameter.trim().toUpperCase();

                synchronized (this) {
                    if (userManager.isUserExists(usernameFromParameter)) {
                       response.getOutputStream().print("Username " + usernameFromParameter + " already exists. Please enter a different username.");

                    }
                    else {
                        //add the new user to the users list
                        ApplicationType type= ApplicationType.valueOf(typeUserNameFromParameter);

                        userManager.addUserName(usernameFromParameter,type);
                        switch (type)
                        {
                            case UBOAT:
                               ServletUtils.getUboatManager().addUboatUser(usernameFromParameter);
                                break;
                            case ALLY:
                                ServletUtils.getAlliesManager().addAllyUser(usernameFromParameter);
                                break;
                            case AGENT:
                               // agentSet.add(username);
                                break;
                        }

                        request.getSession(true).setAttribute(Constants.USERNAME, usernameFromParameter);
                        //redirect the request to the chat room - in order to actually change the URL
                        System.out.println("On login, request URI is: " + request.getRequestURI());
                        //response.sendRedirect(CHAT_ROOM_URL);
                    }
                }
            }
        } else {
            //user is already logged in
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
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
