package Ally;

import CandidateStatus.CandidatesStatusController;
import UBoatDTO.ActiveTeamsDTO;
import agent.AgentDataDTO;
import allyDTOs.AllyCandidateDTO;
import allyDTOs.AllyDataDTO;
import allyDTOs.ContestDataDTO;
import decryptionManager.DecryptionManager;
import engineDTOs.BattlefieldDataDTO;
import engineDTOs.CodeFormatDTO;
import engineDTOs.MachineDataDTO;
import enigmaEngine.Engine;
import enigmaEngine.EnigmaEngine;
import uboat.ContestDataManager;

import java.util.*;

public class SingleAllyController {

    private String uboatManager;
    private String allyName;
    private final Set<AgentDataDTO> agentSet;
    private DecryptionManager decryptionManager;
    private List<AllyCandidateDTO> allyCandidateDTOList;
    private ContestDataDTO contestDataDTO;


    public ContestDataDTO getContestDataDTO() {
        return contestDataDTO;
    }

    public void setContestDataDTO(ContestDataDTO contestDataDTO) {
        this.contestDataDTO = contestDataDTO;
    }

    public SingleAllyController(String allyName) {
        this.agentSet = new HashSet<>();
        uboatManager=null;
        this.allyName=allyName;
        allyCandidateDTOList=new ArrayList<>();
    }

    public List<AllyCandidateDTO> getAllyCandidateDTOList() {
        return Collections.unmodifiableList(allyCandidateDTOList);
    }

    public void addCandidateToAllyList(AllyCandidateDTO allyCandidateDTO)
    {
        allyCandidateDTOList.add(allyCandidateDTO);
    }


    public void setDecryptionManager(CodeFormatDTO initialCode, MachineDataDTO machineDataDTO) {
        this.decryptionManager = new DecryptionManager(initialCode,machineDataDTO);
    }

    public DecryptionManager getDecryptionManager() {
        return decryptionManager;
    }

    public void assignAllyToUboat(String uboatName)
    {
        uboatManager=uboatName;
    }
    public String getUboatNameManager(String allyNam)
    {
        if(uboatManager!=null)
        {
            return uboatManager;
        }
        else
            throw new RuntimeException( allyName+ "is not assign to any Uboat manager");
    }
    public void assignAgentToAlly(AgentDataDTO agentDataDTO)
    {
        agentSet.add(agentDataDTO);
    }

    public Set<AgentDataDTO> getAgentDataForAlly(String allyName)
    {
        return Collections.unmodifiableSet(agentSet);
    }


}
