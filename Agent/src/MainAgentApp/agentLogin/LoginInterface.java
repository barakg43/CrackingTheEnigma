package MainAgentApp.agentLogin;

import agent.AgentDataDTO;

public interface LoginInterface {

    void doLoginRequest(boolean isLoginSuccess, String response, AgentDataDTO userName);


}
