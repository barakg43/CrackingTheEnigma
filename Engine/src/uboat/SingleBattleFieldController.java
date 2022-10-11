package uboat;

import UBoatDTO.ActiveTeamsDTO;
import allyDTOs.AllyDataDTO;
import allyDTOs.ContestDataDTO;
import engineDTOs.BattlefieldDataDTO;
import engineDTOs.CodeFormatDTO;
import engineDTOs.MachineDataDTO;
import enigmaEngine.Engine;
import enigmaEngine.EnigmaEngine;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class SingleBattleFieldController {

    private final Set<AllyDataDTO> alliesDataSet;
    private String xmlFileContent;
    private final Engine enigmaEngine;
    private ContestDataManager contestDataManager;
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

    public Set<AllyDataDTO> getAlliesDataForUboat()
    {
        return Collections.unmodifiableSet(alliesDataSet);
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
    public ActiveTeamsDTO getActiveTeamsDTO()
    {
        return new ActiveTeamsDTO(contestDataManager.getRegisteredAmount(),
                contestDataManager.getRequiredAlliesAmount(),Collections.unmodifiableSet(alliesDataSet));

    }
    public String getXmlFileContent() {
        return xmlFileContent;
    }
}
