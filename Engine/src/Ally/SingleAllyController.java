package Ally;

import agent.AgentDataDTO;
import allyDTOs.AgentsTeamProgressDTO;
import allyDTOs.AllyCandidateDTO;
import allyDTOs.AllyDataDTO;
import decryptionManager.DecryptionManager;
import engineDTOs.DmDTO.GameLevel;
import engineDTOs.DmDTO.TaskFinishDataDTO;
import engineDTOs.MachineDataDTO;

import java.util.*;

public class SingleAllyController {

    private String uboatManager;

    private final Set<AgentDataDTO> activeAgentSet;
    private final Set<AgentDataDTO> waitingListAgentSet;
    private DecryptionManager decryptionManager;
    private final List<AllyCandidateDTO> allyCandidateDTOList;

    private final Map<String, AgentsTeamProgressDTO> agentsTasksProgress;
    private int candidateVersion=0;
    private final AllyDataManager allyDataManager;



    public SingleAllyController(String allyName) {
        this.activeAgentSet = new HashSet<>();
        waitingListAgentSet= new HashSet<>();
        agentsTasksProgress=new HashMap<>();
        uboatManager="";
        allyDataManager=new AllyDataManager(allyName);
        allyCandidateDTOList=new ArrayList<>();
    }
    public void updateAgentProgressData(AgentsTeamProgressDTO teamAgentsDataDTO)
    {
        agentsTasksProgress.put(teamAgentsDataDTO.getAgentName(),teamAgentsDataDTO);

    }
    public List<AgentsTeamProgressDTO> getAgentProgressDTOList()
    {
        return new ArrayList<>(agentsTasksProgress.values());
    }


    public void logoffFromContest()
    {
        uboatManager="";
        allyCandidateDTOList.clear();
        agentsTasksProgress.clear();
        allyDataManager.changeStatus(AllyDataDTO.Status.WAITING_FOR_AGENT);
        activeAgentSet.addAll(waitingListAgentSet);
        waitingListAgentSet.clear();
    }

    public synchronized List<AllyCandidateDTO> getAllyCandidateDTOListWithVersion(int candidatesVersion) {
        if (candidatesVersion < 0 || candidatesVersion > allyCandidateDTOList.size()) {
            candidatesVersion = 0;
        }
        return allyCandidateDTOList.subList(candidatesVersion,allyCandidateDTOList.size());
    }


    public void updateAllyCandidateVersion(){
        candidateVersion=allyCandidateDTOList.size();
    }

    public synchronized void addCandidateToAllyList(TaskFinishDataDTO agentCandidate)
    {
        allyCandidateDTOList.add(new AllyCandidateDTO(agentCandidate, allyDataManager.getAllyName()));

    }
    public AllyDataDTO getAllyDataDTO()
    {
        return allyDataManager;
    }
    public AllyDataManager getAllyDataManager()
    {
        return allyDataManager;
    }

    public void setTaskSize(int taskSize) {
        decryptionManager.setTaskSize(taskSize);
        allyDataManager.setTaskSize(taskSize);
    }
    public boolean isWaitingAgent(String agentName)
    {

        for(AgentDataDTO agentDataDTO:waitingListAgentSet)
        {
            if(agentDataDTO.getAgentName().equals(agentName))
                return true;
        }
        return false;
    }
    public void createDecryptionManager( MachineDataDTO machineDataDTO,GameLevel level) {

        this.decryptionManager = new DecryptionManager(machineDataDTO,level,allyDataManager.getAllyName());
    }

    public DecryptionManager getDecryptionManager() {
        return decryptionManager;
    }

    public void assignAllyToUboat(String uboatName)
    {
        uboatManager=uboatName;
    }

    public String getUboatNameManager()
    {
            return uboatManager;
    }
    public synchronized void assignAgentToAlly(AgentDataDTO agentDataDTO)
    {
        if(allyDataManager.getStatus()== AllyDataDTO.Status.WAITING_FOR_AGENT) {
            if (activeAgentSet.add(agentDataDTO))
                allyDataManager.increaseAgentNumber();
        }
        else
            waitingListAgentSet.add(agentDataDTO);
    }

    public List<AgentDataDTO> getAgentDataListForAlly()
    {
        return Collections.unmodifiableList(new ArrayList<>(activeAgentSet));
    }


}
