package menuEngine;

import dtoObjects.*;

import java.util.List;

public interface Engine {

    MachineDataDTO getMachineData();

    SelectedConfigurationDTO getSelectedData();

     void LoadXMLFile(String filePath);

     void checkIfRotorsValid(String rotors);

     void checkIfDataValid(String data);

    StatisticsDataDTO getStatisticDataDTO();

    void checkIfPositionsValid(String positions);

    String cipherData(String dataInput);

    void resetCodePosition();

    String getCodeFormat();

    void resetSelected();

    void checkIfReflectorNumValid(String ReflectorNum);
    int getNumberOfRotorInSystem();
    List<String> getReflectorIdList();
    void checkPlugBoardPairs(String pairs) ;
    void  saveMachineStateToFile(String filePathNoExtension);
    int checkPlugBoardNum(String plugBoardNum);
    void loadMachineStateFromFile(String filePathNoExtension);
    void setCodeAutomatically();

    boolean getWithPlugBoardPairs();
    int getCipheredInputs();
    @Override
    String toString();
    void resetAllData();
    boolean isMachineLoaded();


}
