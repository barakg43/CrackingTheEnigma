package uboat;

import CandidateStatus.CandidatesStatusController;
import UBoatDTO.ActiveTeamsDTO;
import allyDTOs.AllyDataDTO;
import allyDTOs.ContestDataDTO;
import engineDTOs.BattlefieldDataDTO;
import enigmaEngine.Engine;
import enigmaEngine.EnigmaEngine;

import java.util.*;

public class SingleBattleFieldController {

    private final Set<AllyDataDTO> alliesDataSet;
    private String xmlFileContent;
    private final Engine enigmaEngine;
    private ContestDataManager contestDataManager;

    private CandidatesStatusController candidatesStatusController;
    private final String uboatName;
    public SingleBattleFieldController(String uboatName) {
        alliesDataSet=new HashSet<>();
        enigmaEngine=new EnigmaEngine();
        this.uboatName=uboatName;
    }
    public void assignAllyToUboat(AllyDataDTO agentDataDTO)
    {
            alliesDataSet.add(agentDataDTO);
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
    public String getXmlFileContent() {
        return xmlFileContent;
    }
}
