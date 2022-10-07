package MainAgentApp;

import java.net.URL;

public class CommonResources {
 public final static String AGENT_MAIN_APP_FXML_LOGIN = "MainAgentApp/MainAgent.fxml";
 public final static String AGENT_APP_FXML_LOGIN = "agentLogin/AgainLogin.fxml";
 public final static String AGENT_APP_FXML_INCLUDE_RESOURCE = "AgentApp/agent.fxml";

 public static final String CANDIDATE_SINGLE_TILE = "CandidateStatus/singleCandidate/singleCandidate.fxml";
 public static final URL MAIN_FXML_RESOURCE = CommonResources.class.getResource(CommonResources.CANDIDATE_SINGLE_TILE);

}
