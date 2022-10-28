package uboat;

import UBoatDTO.ActiveTeamsDTO;
import UBoatDTO.GameStatus;
import agent.AgentSetupConfigurationDTO;
import allyDTOs.AllyDataDTO;
import allyDTOs.ContestDataDTO;
import engineDTOs.BattlefieldDataDTO;
import engineDTOs.CodeFormatDTO;
import enigmaEngine.Engine;
import enigmaEngine.EnigmaEngine;

import java.util.*;
import java.util.function.Consumer;

public class SingleBattleFieldController {

    private final Set<AllyDataDTO> alliesDataSet;
    private final Consumer<String> startContestInAllies;
    private String xmlFileContent;
    private String winnerName;


    private  CodeFormatDTO codeFormatConfiguration;
    private  String cipheredString;
    private final Engine enigmaEngine;
    private ContestDataManager contestDataManager;

    private final String uboatName;
    public SingleBattleFieldController(String uboatName, Consumer<String> startContestInAllies) {
        this.startContestInAllies = startContestInAllies;
        alliesDataSet=new HashSet<>();
        enigmaEngine=new EnigmaEngine();
        this.uboatName=uboatName;
        winnerName="";
    }

    public synchronized void assignAllyToUboat(AllyDataDTO agentDataDTO)
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


    public synchronized void checkIfAllReady()
    {
        boolean isAllReady=true;
        for(AllyDataDTO allyData:alliesDataSet)
        {
            isAllReady=isAllReady&&(allyData.getStatus()== AllyDataDTO.Status.READY);
        }
        if(isAllReady &&
                contestDataManager.getGameStatus()== GameStatus.WAITING_FOR_ALLIES &&
                contestDataManager.getRegisteredAmount()==contestDataManager.getRequiredAlliesAmount())
                                contestDataManager.changeGameStatus(GameStatus.ACTIVE);
        if(contestDataManager.getGameStatus()==GameStatus.ACTIVE)
            startContestInAllies.accept(uboatName);//TODO: fix start contest

    }
    public CodeFormatDTO getCodeFormatConfiguration() {
        return codeFormatConfiguration;
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
                contestDataManager.getRequiredAlliesAmount(),getContestDataDTO().getGameStatus(),Collections.unmodifiableSet(alliesDataSet));

    }
    public AgentSetupConfigurationDTO getAgentSetupConfigurationDTO()
    {
        return new AgentSetupConfigurationDTO(xmlFileContent,codeFormatConfiguration,cipheredString);
    }
    public void setContestInitConfiguration(String cipheredString) {
        this.codeFormatConfiguration = enigmaEngine.getCodeFormat(true);
        this.cipheredString=cipheredString;
    }

    public void setWinnerName(String winnerName) {
        this.winnerName = winnerName;
    }

    public String getWinnerName() {
        return winnerName;
    }
}
