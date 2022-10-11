package shared.servlets.listener;

import com.google.gson.Gson;
import enigmaEngine.EnigmaEngine;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import users.AlliesManager;
import users.UBoatManager;
import users.UserManager;
import utils.ServletUtils;

import static utils.ServletUtils.*;

@WebListener
public class MainContextListener implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        System.out.println("Battlefield Manager is being initialized :)");
        ServletContext servletContext=servletContextEvent.getServletContext();
        ServletUtils.setServletContextRef(servletContext);
        servletContext.setAttribute(GSON_ATTRIBUTE_NAME, new Gson());
        servletContext.setAttribute(USER_MANAGER_ATTRIBUTE_NAME, new UserManager());
        servletContext.setAttribute(UBOAT_MANAGER_ATTRIBUTE_NAME, new UBoatManager());
        servletContext.setAttribute(ALLY_MANAGER_ATTRIBUTE_NAME, new AlliesManager());

        System.out.println("Battlefield Manager finish initialized.");
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {

        System.out.println("My web app is being destroyed ... :(");
    }
}
