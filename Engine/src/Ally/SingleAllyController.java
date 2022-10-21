package Ally;

import agent.AgentDataDTO;
import allyDTOs.AllyCandidateDTO;
import allyDTOs.AllyDataDTO;
import allyDTOs.TeamAgentsDataDTO;
import decryptionManager.DecryptionManager;
import engineDTOs.CodeFormatDTO;
import engineDTOs.MachineDataDTO;

import java.util.*;

public class SingleAllyController {

    private String uboatManager;
    private final String allyName;
    private final Set<AgentDataDTO> agentSet;
    private DecryptionManager decryptionManager;
    private final List<AllyCandidateDTO> allyCandidateDTOList;

    private final Map<String, TeamAgentsDataDTO> agentsTasksProgress;
    private int candidateVersion=0;
    private final AllyDataManager allyDataManager;
    public SingleAllyController(String allyName) {
        this.agentSet = new HashSet<>();
        agentsTasksProgress=new HashMap<>();
        uboatManager=null;
        this.allyName=allyName;
        allyDataManager=new AllyDataManager(allyName);
        allyCandidateDTOList=new ArrayList<>();
    }
    public void updateAgentProgressData(TeamAgentsDataDTO teamAgentsDataDTO)
    {
        agentsTasksProgress.put(teamAgentsDataDTO.getAgentName(),teamAgentsDataDTO);

    }
    public List<TeamAgentsDataDTO> getAgentProgressDTOList()
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

    public void setDecryptionManager(CodeFormatDTO initialCode, MachineDataDTO machineDataDTO) {
        this.decryptionManager = new DecryptionManager(initialCode,machineDataDTO);
    }
    public void setTaskSize(int taskSize)
    {
        allyDataManager.setTaskSize(taskSize);
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
        if(uboatManager!=null)
        {
            return uboatManager;
        }
        else
            throw new RuntimeException( allyName+ "is not assign to any Uboat manager");
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
