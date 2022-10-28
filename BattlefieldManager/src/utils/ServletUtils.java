package utils;


import com.google.gson.Gson;
import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import systemManager.SystemManager;

import java.io.IOException;
import java.io.PrintWriter;

import static general.ConstantsHTTP.INT_PARAMETER_ERROR;


public class ServletUtils {

	public static final String SYSTEM_MANAGER_ATTRIBUTE_NAME = "userManager";
	public static final String UBOAT_MANAGER_ATTRIBUTE_NAME = "uboatManager";

	public static final String ALLY_MANAGER_ATTRIBUTE_NAME = "allyManager";

	public static final String GSON_ATTRIBUTE_NAME = "gson";

	public static final String AGENT_MANAGER_ATTRIBUTE_NAME = "agentManager";
	private static ServletContext servletContextRef;
	/*
	Note how the synchronization is done only on the question and\or creation of the relevant managers and once they exists -
	the actual fetch of them is remained un-synchronized for performance POV
	 */

	public static Gson getGson() {

		return (Gson) servletContextRef.getAttribute(GSON_ATTRIBUTE_NAME);
	}
	public static SystemManager getSystemManager() {
		return (SystemManager) servletContextRef.getAttribute(SYSTEM_MANAGER_ATTRIBUTE_NAME);
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
	public static void setBadRequestErrorResponse(Throwable errorResponse, HttpServletResponse response) throws IOException {
		PrintWriter out=response.getWriter();
		response.setContentType("text/plain");
		out.println(errorResponse.getMessage());
		errorResponse.printStackTrace();
		out.flush();
		response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
	}
}
