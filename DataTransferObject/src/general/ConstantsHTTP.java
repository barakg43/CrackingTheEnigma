package general;



public class ConstantsHTTP {

    // global constants
    public final static String LINE_SEPARATOR = System.getProperty("line.separator");

    public final static int REFRESH_RATE = 2000;


    // Server resources locations
    public final static String BASE_DOMAIN = "localhost";
    private final static String BASE_URL = "http://" + BASE_DOMAIN + ":8080";
    private final static String CONTEXT_PATH = "/BattlefieldManager";
    public final static String FULL_SERVER_PATH = BASE_URL + CONTEXT_PATH;
    public final static String UPLOAD_FILE = "/upload-file";
    public final static String LOGIN_PAGE = "/login";
//    public final static String USERS_LIST = FULL_SERVER_PATH + "/userslist";
//    public final static String LOGOUT = FULL_SERVER_PATH + "/chat/logout";
//    public final static String SEND_CHAT_LINE = FULL_SERVER_PATH + "/pages/chatroom/sendChat";
//    public final static String CHAT_LINES_LIST = FULL_SERVER_PATH + "/chat";

    // GSON instance

}
