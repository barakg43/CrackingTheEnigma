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
    public final static String LOGIN = "/login";
    public static final String USER_LIST = "/user-list";


    /**################ UBOAT URLs ####################**/

    public final static String UBOAT_CONTEXT="/uboat";
    public final static String UPLOAD_FILE = "/upload-file";
    public static final String ACTIVE_TEAMS_LIST ="/active-teams-list";
    public static final String INPUT_STRING = "/input-string";
    public static final String MACHINE_DATA = "/machine-data";
    public static final String CANDIDATES = "/candidates";
    public static final String DICTIONARY_WORDS = "/dictionary-words";
    public static final String AUTOMATIC_CODE = "/automatic-code";
    public static final String MANUALLY_CODE = "/manually-code";
    public static final String RESET_CODE = "/reset-code";
    public static final String RESET_MACHINE = "/reset-machine";
    public static final String READY_TO_START = "/ready-to-start";


    /**################ ALLY URLs ####################**/
    public final static  String ALLY_CONTEXT="/ally";


    /**################ AGENT URLs ####################**/
    public static final String AGENT_CONTEXT="/agent";

//    public final static String USERS_LIST = FULL_SERVER_PATH + "/userslist";
//    public final static String LOGOUT = FULL_SERVER_PATH + "/chat/logout";
//    public final static String SEND_CHAT_LINE = FULL_SERVER_PATH + "/pages/chatroom/sendChat";
//    public final static String CHAT_LINES_LIST = FULL_SERVER_PATH + "/chat";

    // GSON instance

}
