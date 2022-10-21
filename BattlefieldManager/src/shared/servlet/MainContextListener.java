package shared.servlet;

import com.google.gson.Gson;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import systemManager.SystemManager;
import utils.ServletUtils;

import static utils.ServletUtils.GSON_ATTRIBUTE_NAME;
import static utils.ServletUtils.SYSTEM_MANAGER_ATTRIBUTE_NAME;

@WebListener
public class MainContextListener implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        System.out.println("Battlefield Manager is being initialized :)");
        ServletContext servletContext=servletContextEvent.getServletContext();
        ServletUtils.setServletContextRef(servletContext);
        servletContext.setAttribute(GSON_ATTRIBUTE_NAME, new Gson());
        servletContext.setAttribute(SYSTEM_MANAGER_ATTRIBUTE_NAME, new SystemManager());



        System.out.println("Battlefield Manager finish initialized.");
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {

        System.out.println("My web app is being destroyed ... :(");
    }
}
