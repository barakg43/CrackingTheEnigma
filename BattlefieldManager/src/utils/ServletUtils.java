package utils;


import com.google.gson.Gson;
import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpServletRequest;
import users.AgentManager;
import users.AlliesManager;
import users.UBoatManager;
import users.UserManager;

import static constants.Constants.INT_PARAMETER_ERROR;


public class ServletUtils {

	public static final String USER_MANAGER_ATTRIBUTE_NAME = "userManager";
	public static final String UBOAT_MANAGER_ATTRIBUTE_NAME = "uboatManager";

	public static final String ALLY_MANAGER_ATTRIBUTE_NAME = "allyManager";

	public static final String GSON_ATTRIBUTE_NAME = "gson";

	public static final String AGENT_MANAGER_ATTRIBUTE_NAME = "agentManager";
	private static ServletContext servletContextRef;
	/*
	Note how the synchronization is done only on the question and\or creation of the relevant managers and once they exists -
	the actual fetch of them is remained un-synchronized for performance POV
	 */

	//private static final Object userManagerLock = new Object();
	public static Gson getGson() {
//		synchronized (userManagerLock) {
//			if (servletContext.getAttribute(GSON_ATTRIBUTE_NAME) == null) {
//				servletContext.setAttribute(GSON_ATTRIBUTE_NAME, new Gson());
//			}
//		}
		// Initialized in class servlets.listener.MainContextListener
		return (Gson) servletContextRef.getAttribute(GSON_ATTRIBUTE_NAME);
	}
	public static UserManager getSystemUserManager() {
//		synchronized (userManagerLock) {
//			if (servletContext.getAttribute(USER_MANAGER_ATTRIBUTE_NAME) == null) {
//				servletContext.setAttribute(USER_MANAGER_ATTRIBUTE_NAME, new UserManager());
//			}
//		}
		// Initialized in class servlets.listener.MainContextListener
		return (UserManager) servletContextRef.getAttribute(USER_MANAGER_ATTRIBUTE_NAME);
	}
	public static UBoatManager getUboatManager() {
//		synchronized (userManagerLock) {
//					if (servletContext.getAttribute(UBOAT_MANAGER_ATTRIBUTE_NAME) == null) {
//						servletContext.setAttribute(UBOAT_MANAGER_ATTRIBUTE_NAME, new UBoatManager());
//					}
//				}
				// Initialized in class servlets.listener.MainContextListener
				return (UBoatManager) servletContextRef.getAttribute(UBOAT_MANAGER_ATTRIBUTE_NAME);
		}

		public static AgentManager getAgentManager(){
		return (AgentManager) servletContextRef.getAttribute(AGENT_MANAGER_ATTRIBUTE_NAME);
		}
	public static AlliesManager getAlliesManager() {
//		synchronized (userManagerLock) {
//			if (servletContext.getAttribute(ALLY_MANAGER_ATTRIBUTE_NAME) == null) {
//				servletContext.setAttribute(ALLY_MANAGER_ATTRIBUTE_NAME, new AlliesManager());
//			}
//		}
		// Initialized in class servlets.listener.MainContextListener
		return (AlliesManager) servletContextRef.getAttribute(ALLY_MANAGER_ATTRIBUTE_NAME);
	}
	public static void logRequestAndTime(String title, String description)
	{
		long time = System.currentTimeMillis();
		String CHAT_LINE_FORMATTING = "%tH:%tM:%tS:%tL | %s: %s%n";
		System.out.format( CHAT_LINE_FORMATTING, time, time, time,time, title, description);
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

	public static void setServletContextRef(ServletContext servletContextRef) {
		ServletUtils.servletContextRef = servletContextRef;
	}
}
