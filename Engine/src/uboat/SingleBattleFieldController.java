package uboat;

import UBoatDTO.ActiveTeamsDTO;
import agent.AgentSetupConfigurationDTO;
import allyDTOs.AllyDataDTO;
import allyDTOs.ContestDataDTO;
import application.UBoatApp.ContestTab.CandidateStatus.CandidatesStatusController;
import engineDTOs.BattlefieldDataDTO;
import engineDTOs.CodeFormatDTO;
import enigmaEngine.Engine;
import enigmaEngine.EnigmaEngine;

import java.util.*;

public class SingleBattleFieldController {

    private final Set<AllyDataDTO> alliesDataSet;
    private String xmlFileContent;
    private  CodeFormatDTO codeFormatConfiguration;
    private  String cipheredString;
    private final Engine enigmaEngine;
    private ContestDataManager contestDataManager;

    private CandidatesStatusController candidatesStatusController;
    private final String uboatName;
    public SingleBattleFieldController(String uboatName) {
        alliesDataSet=new HashSet<>();
        enigmaEngine=new EnigmaEngine();
        this.uboatName=uboatName;
    }
    public void  assignAllyToUboat(AllyDataDTO agentDataDTO)
    {
        if(contestDataManager.getRegisteredAmount()==contestDataManager.getRequiredAlliesAmount())
            throw new RuntimeException(contestDataManager.getBattlefieldName()+" is already full!");

        if(alliesDataSet.add(agentDataDTO))//if not already exist
            contestDataManager.incrementRegisterAmount();

    }

    public List<AllyDataDTO> getAlliesDataListForUboat()
    {
        return new ArrayList<>(alliesDataSet);
    }

    public Engine getEnigmaEngine() {
        return enigmaEngine;
    }


    public void assignXMLFileToUboat(String XmlContent)
    {

        xmlFileContent=XmlContent;
        enigmaEngine.loadXMLFileFromStringContent(XmlContent);
        contestDataManager=new ContestDataManager(uboatName,enigmaEngine.getBattlefieldDataDTO());
    }

    public ContestDataDTO getContestDataDTO() {
        return contestDataManager;
    }
    public BattlefieldDataDTO getBattlefieldDataDTO() {
        return contestDataManager;
    }
    public ContestDataManager getContestDataManager()
    {
        return contestDataManager;
    }
    public ActiveTeamsDTO getActiveTeamsDTO()
    {
        return new ActiveTeamsDTO(contestDataManager.getRegisteredAmount(),
                contestDataManager.getRequiredAlliesAmount(),Collections.unmodifiableSet(alliesDataSet));

    }
    public AgentSetupConfigurationDTO getAgentSetupConfigurationDTO()
    {
        return new AgentSetupConfigurationDTO(xmlFileContent,codeFormatConfiguration,cipheredString);
    }
    public void setContestInitConfiguration(String cipheredString) {
        this.codeFormatConfiguration = enigmaEngine.getCodeFormat(true);
        this.cipheredString=cipheredString;
    }
}
