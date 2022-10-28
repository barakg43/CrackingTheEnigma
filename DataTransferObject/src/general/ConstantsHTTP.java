package general;


public class ConstantsHTTP {

    // global constants
    public final static String LINE_SEPARATOR = System.getProperty("line.separator");

    public final static int REFRESH_RATE = 10000;
    public final static int FAST_REFRESH_RATE = 500;
    public static final String CANDIDATES_VERSION_PARAMETER = "candidates-version";

    public static final int INT_PARAMETER_ERROR = Integer.MIN_VALUE;
    public static final String USERNAME = "username";
    public static final String AMOUNT="amount";
    public static final String WINNER_NAME="winner-name";
    public static final String INPUT_PROPERTY = "input";
    public static final String OUTPUT_PROPERTY = "output";
    public static final String TASK_SIZE = "task-size";
    public static final String TOTAL_TASK_AMOUNT = "total-task";
    public static final String QUERY_FORMAT="%s?%s=%s";
    public static final String SINGLE_JSON_FORMAT="{\"%s\":%s}";
    // Server resources locations
    public final static String BASE_DOMAIN = "localhost";
    private final static String BASE_URL = "http://" + BASE_DOMAIN + ":8080";
    private final static String CONTEXT_PATH = "/BattlefieldManager";
    public final static String FULL_SERVER_PATH = BASE_URL + CONTEXT_PATH;
    /**################ Common URLs ####################**/
    public final static String LOGIN = "/login";
    public final static String LOGOUT = "/logout";
    public static final String USER_LIST = "/user-list";
    public static final String  UPDATE_CANDIDATES="/update-candidates";
//    public static final String CONTEST_DATA="/update-contest-data";
    public static final String UPDATE_DASHBOARD="/update-dashboard-data";
    public static final String UPDATE_CONTEST="/update-contest-data";
    public static final String READY_TO_START = "/ready-to-start";
    public static final String WINNER_TEAM = "/winner-team";

    /**################ UBOAT URLs ####################**/

    public final static String UBOAT_CONTEXT="/uboat";
    public final static String UPLOAD_FILE = "/upload-file";
    public static final String ACTIVE_TEAMS_LIST ="/active-teams-list";
    public static final String INPUT_STRING = "/input-string";
    public static final String MACHINE_DATA = "/machine-data";

    public static final String DICTIONARY_WORDS = "/dictionary-words";
    public static final String AUTOMATIC_CODE = "/automatic-code";
    public static final String ALL_CODE = "/code-configuration";
    public static final String MANUALLY_CODE = "/manually-code";
    public static final String RESET_CODE = "/reset-code";
    public static final String RESET_MACHINE = "/reset-machine";


    /**################ ALLY URLs ####################**/
    public final static  String ALLY_CONTEXT="/ally";
    public static final String REGISTER_TO_UBOAT = "/register-to-uboat";
    public static final String START_TASKS_CREATOR = "/start-task-creator";
    public static final String UBOAT_PARAMETER = ApplicationType.UBOAT.toString();

    /**################ AGENT URLs ####################**/
    public static final String AGENT_CONTEXT="/agent";
    public static final String UPDATE_PROGRESS = "/update-progress";
    public static final String AGENT_CONFIGURATION = "/configuration";

    public static final String  GET_TASKS="/get-tasks";


//    public final static String USERS_LIST = FULL_SERVER_PATH + "/userslist";
//    public final static String LOGOUT = FULL_SERVER_PATH + "/chat/logout";
//    public final static String SEND_CHAT_LINE = FULL_SERVER_PATH + "/pages/chatroom/sendChat";
//    public final static String CHAT_LINES_LIST = FULL_SERVER_PATH + "/chat";

    // GSON instance

}
