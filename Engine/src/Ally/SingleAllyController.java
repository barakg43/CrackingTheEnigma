package Ally;

import agent.AgentDataDTO;
import allyDTOs.AllyCandidateDTO;
import allyDTOs.AllyDataDTO;
import allyDTOs.AgentsTeamProgressDTO;
import decryptionManager.DecryptionManager;
import engineDTOs.CodeFormatDTO;
import engineDTOs.DmDTO.GameLevel;
import engineDTOs.MachineDataDTO;

import java.util.*;

public class SingleAllyController {

    private String uboatManager;

    private final Set<AgentDataDTO> agentSet;
    private DecryptionManager decryptionManager;
    private final List<AllyCandidateDTO> allyCandidateDTOList;

    private final Map<String, AgentsTeamProgressDTO> agentsTasksProgress;
    private int candidateVersion=0;
    private final AllyDataManager allyDataManager;



    public SingleAllyController(String allyName) {
        this.agentSet = new HashSet<>();
        agentsTasksProgress=new HashMap<>();
        uboatManager=null;
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
    public List<AllyCandidateDTO> getAllyCandidateDTOListWithVersion() {

        return allyCandidateDTOList.subList(candidateVersion,allyCandidateDTOList.size());
    }
    public void updateAllyCandidateVersion(){
        candidateVersion=allyCandidateDTOList.size();
    }

    public void addCandidateToAllyList(AllyCandidateDTO allyCandidateDTO)
    {
        allyCandidateDTOList.add(allyCandidateDTO);

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

    public void createDecryptionManager( MachineDataDTO machineDataDTO,GameLevel level) {

        this.decryptionManager = new DecryptionManager(machineDataDTO,level);
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
        if(agentSet.add(agentDataDTO))
            allyDataManager.increaseAgentNumber();
    }

    public List<AgentDataDTO> getAgentDataListForAlly()
    {
        return Collections.unmodifiableList(new ArrayList<>(agentSet));
    }


}
