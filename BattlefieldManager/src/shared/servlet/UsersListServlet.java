package shared.servlet;

import com.google.gson.Gson;
import general.UserListDTO;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import users.UserManager;
import utils.ServletUtils;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Set;

import static general.ConstantsHTTP.*;

@WebServlet(name = "UserListServlet", urlPatterns = {USER_LIST,UBOAT_CONTEXT+USER_LIST,ALLY_CONTEXT+USER_LIST,AGENT_CONTEXT+USER_LIST})
public class UsersListServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        //returning JSON objects, not HTML
        response.setContentType("application/json");
        try (PrintWriter out = response.getWriter()) {
            Gson gson = ServletUtils.getGson();
            UserManager userManager = ServletUtils.getSystemUserManager();
            UserListDTO usersList = userManager.getUsers();
            String json = gson.toJson(usersList);
            out.println(json);
            out.flush();
        }
    }

}
