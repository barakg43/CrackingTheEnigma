package enigmaEngine;

import dtoObjects.*;

import java.util.List;


public interface Engine extends Encryptor {
    void loadXMLFile(String filePath);
    void checkIfRotorsValid(List<Integer> arrayString);
    void checkIfPositionsValid(List<Character> positions);
    void checkIfReflectorNumValid(String ReflectorNum);
    void checkPlugBoardPairs(List<PlugboardPairDTO> plugBoardPairs) ;
    public void setReflector(String reflector);

    StatisticsDataDTO getStatisticDataDTO();
    MachineDataDTO getMachineData();
    void resetSelected();
    void setCodeAutomatically();
    boolean getWithPlugBoardPairs();
    int getCipheredInputsAmount();
    void  saveMachineStateToFile(String filePathNoExtension);
    boolean isCodeConfigurationIsSet();
    @Override
    String toString();
    void resetAllData();
    boolean isMachineLoaded();
    CodeFormatDTO getCodeFormat(boolean isCalcDistanceFromInitWindow);
    public void bruteForce(CodeFormatDTO codeFormatDTO, bruteForceLevel BFLevel);
    public CodeFormatDTO getBFCodeFormat();

}
