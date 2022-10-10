package utils;


import com.google.gson.Gson;
import enigmaEngine.Engine;
import enigmaEngine.EnigmaEngine;
import general.ApplicationType;
import jakarta.servlet.GenericServlet;
import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import users.AlliesManager;
import users.UBoatManager;
import users.UserManager;

import static constants.Constants.INT_PARAMETER_ERROR;


public class ServletUtils {

	public static final String USER_MANAGER_ATTRIBUTE_NAME = "userManager";
	public static final String UBOAT_MANAGER_ATTRIBUTE_NAME = "uboatManager";
	public static final String ALLY_MANAGER_ATTRIBUTE_NAME = "allyManager";
	public static final String ENGINE_ATTRIBUTE_NAME = "engine";
	public static final String GSON_ATTRIBUTE_NAME = "gson";
	private static final String AGENT_MANAGER_ATTRIBUTE_NAME = "agentManager";
	/*
	Note how the synchronization is done only on the question and\or creation of the relevant managers and once they exists -
	the actual fetch of them is remained un-synchronized for performance POV
	 */
	private static final Object userManagerLock = new Object();
	public static Gson getGson(ServletContext servletContext) {
//		synchronized (userManagerLock) {
//			if (servletContext.getAttribute(GSON_ATTRIBUTE_NAME) == null) {
//				servletContext.setAttribute(GSON_ATTRIBUTE_NAME, new Gson());
//			}
//		}
		// Initialized in class servlets.listener.MainContextListener
		return (Gson) servletContext.getAttribute(GSON_ATTRIBUTE_NAME);
	}
	public static UserManager getSystemUserManager(ServletContext servletContext) {
//		synchronized (userManagerLock) {
//			if (servletContext.getAttribute(USER_MANAGER_ATTRIBUTE_NAME) == null) {
//				servletContext.setAttribute(USER_MANAGER_ATTRIBUTE_NAME, new UserManager());
//			}
//		}
		// Initialized in class servlets.listener.MainContextListener
		return (UserManager) servletContext.getAttribute(USER_MANAGER_ATTRIBUTE_NAME);
	}
	public static UBoatManager getUboatManager(ServletContext servletContext) {
//		synchronized (userManagerLock) {
//					if (servletContext.getAttribute(UBOAT_MANAGER_ATTRIBUTE_NAME) == null) {
//						servletContext.setAttribute(UBOAT_MANAGER_ATTRIBUTE_NAME, new UBoatManager());
//					}
//				}
				// Initialized in class servlets.listener.MainContextListener
				return (UBoatManager) servletContext.getAttribute(UBOAT_MANAGER_ATTRIBUTE_NAME);
		}

	public static AlliesManager getAlliesManager(ServletContext servletContext) {
//		synchronized (userManagerLock) {
//			if (servletContext.getAttribute(ALLY_MANAGER_ATTRIBUTE_NAME) == null) {
//				servletContext.setAttribute(ALLY_MANAGER_ATTRIBUTE_NAME, new AlliesManager());
//			}
//		}
		// Initialized in class servlets.listener.MainContextListener
		return (AlliesManager) servletContext.getAttribute(ALLY_MANAGER_ATTRIBUTE_NAME);
	}

//	public static ChatManager getChatManager(ServletContext servletContext) {
//		synchronized (chatManagerLock) {
//			if (servletContext.getAttribute(CHAT_MANAGER_ATTRIBUTE_NAME) == null) {
//				servletContext.setAttribute(CHAT_MANAGER_ATTRIBUTE_NAME, new ChatManager());
//			}
//		}
//		return (ChatManager) servletContext.getAttribute(CHAT_MANAGER_ATTRIBUTE_NAME);
//	}

	public static Engine getEngine(ServletContext servletContext) {
//		synchronized (userManagerLock) {
//			if (servletContext.getAttribute(ENGINE_ATTRIBUTE_NAME) == null) {
//				servletContext.setAttribute(ENGINE_ATTRIBUTE_NAME, new EnigmaEngine());
//			}
//		}
		// Initialized in class servlets.listener.MainContextListener
		return (Engine) servletContext.getAttribute(ENGINE_ATTRIBUTE_NAME);
	}

	public static int getIntParameter(HttpServletRequest request, String name) {
		String value = request.getParameter(name);
		if (value != null) {
			try {
				return Integer.parseInt(value);
			} catch (NumberFormatException numberFormatException) {
			}
		}
		return INT_PARAMETER_ERROR;
	}
}
