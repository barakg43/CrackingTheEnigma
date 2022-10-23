package enigmaEngine;

import decryptionManager.components.Dictionary;
import engineDTOs.*;
import engineDTOs.DmDTO.GameLevel;
import enigmaMachine.EnigmaMachine;
import enigmaMachine.parts.Reflector;
import enigmaMachine.parts.Rotor;
import javafx.beans.property.SimpleStringProperty;
import jaxb.CTEBattlefield;
import jaxb.CTEEnigma;
import jaxb.CTERotor;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class EnigmaEngine implements Engine , Serializable {

    private static final String JAXB_XML_PACKAGE_NAME = "jaxb";
    private static final String stateMachineFileExtension=".bin";
    private EnigmaMachine enigmaMachine;
    private MachineDataDTO machineData;
    private List<PlugboardPairDTO> plugBoardPairs;
    private int agentAmount;
    private StatisticsData statisticsData;
    private BattlefieldDataDTO battlefieldDataDTO;
    private Set<String> loadedBattlefieldName;

    private Reflector selectedReflector = null;
    private char[] selectedPositions;
    private Rotor[] selectedRotors;
    private int cipheredInputsAmount;
    private CodeFormat initialCodeFormat;
    private  char[] tempSelectedInitPositions;
    private  int tempSelectedReflectorID;
    private List<Integer> tempSelectedRotorsID;
    private  List<PlugboardPairDTO> tempPlugBoardPairs;

    private long sumProcessingTime=0;
     private Dictionary dictionary;



    @Override
    public boolean isMachineLoaded()
    {
        return enigmaMachine!=null;
    }

    public EnigmaEngine() {
        enigmaMachine = null;
        statisticsData=new StatisticsData();
        cipheredInputsAmount =0;
        initialCodeFormat=null;
        loadedBattlefieldName=new HashSet<>();
        plugBoardPairs=new ArrayList<>();
    }

    public void resetAllData()
    {

        plugBoardPairs=new ArrayList<>();
        statisticsData=new StatisticsData();
        selectedRotors=null;
        selectedPositions=null;
        selectedReflector=null;
        cipheredInputsAmount =0;
    }


    @Override
    public MachineDataDTO getMachineData() {

        return machineData;
    }


    @Override
    public boolean isCodeConfigurationIsSet() {
        return initialCodeFormat!=null;
    }

    @Override
    public void loadXMLFile(String filePath) {
        filePath=filePath.replaceAll("\"","");//for case user enter with " "

        try {
            JAXBContext jc = JAXBContext.newInstance(JAXB_XML_PACKAGE_NAME);
            Unmarshaller u = jc.createUnmarshaller();
            CTEEnigma eng = (CTEEnigma) u.unmarshal(Files.newInputStream(Paths.get(filePath)));
            copyAllData(eng);
        } catch (JAXBException e) {
            e.printStackTrace();
            throw new RuntimeException("Error on parsing xml file!");
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }


    }
    public void loadXMLFileFromStringContent(String inputStreamXml)
    {

        try {
            JAXBContext jc = JAXBContext.newInstance(JAXB_XML_PACKAGE_NAME);
            Unmarshaller u = jc.createUnmarshaller();
            Reader xmlReader = new StringReader(inputStreamXml);
            CTEEnigma eng = (CTEEnigma) u.unmarshal(xmlReader);
            xmlReader.close();
            copyAllData(eng);

        } catch (JAXBException e) {
            e.printStackTrace();
            throw new RuntimeException("Error on parsing xml file!");
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }

    }
    @Override
    public void setCodeManually(CodeFormatDTO codeConfiguration) {
        RotorInfoDTO[] rotorInfoDTO=codeConfiguration.getRotorInfoArray();

        Integer[] rotorIds=new Integer[rotorInfoDTO.length];
        Character[] positions=new Character[rotorInfoDTO.length];
            for(int i=0;i<rotorInfoDTO.length;i++)
            {       rotorIds[i]=rotorInfoDTO[i].getId();
                    positions[i]=rotorInfoDTO[i].getStatingLetter();

            }

         List<Integer> rotorIdsList=Arrays.asList(rotorIds);
         List<Character> positionsList=Arrays.asList(positions);
         Collections.reverse(rotorIdsList);
         Collections.reverse(positionsList);
         checkIfRotorsValid(rotorIdsList);
         checkIfPositionsValid(positionsList);
         int reflectorId= Reflector.convertRomanIdToNumber(codeConfiguration.getReflectorID());
         checkIfReflectorNumValid(String.valueOf(reflectorId));
         checkPlugBoardPairs(codeConfiguration.getPlugboardPairDTOList()) ;

    }

    @Override
    public Dictionary getDictionary() {
        return dictionary;
    }

    @Override
    public void checkIfRotorsValid( List<Integer> arrayInteger) {
        tempSelectedRotorsID = new ArrayList<>(enigmaMachine.getRotorNumberInUse());
         int rotorNum;
        if (arrayInteger.size() != enigmaMachine.getRotorNumberInUse())
            throw new RuntimeException("You need to choose " + enigmaMachine.getRotorNumberInUse() + " rotors.");

        for (int j = arrayInteger.size() - 1; j >= 0; j--) {
            rotorNum= arrayInteger.get(j);
            if (tempSelectedRotorsID.contains(rotorNum))
                throw new RuntimeException("You select the same rotor twice.");
            tempSelectedRotorsID.add(rotorNum);

        }
    }

    @Override
    public int getAgentsAmount() {
        return agentAmount;
    }

    @Override
    public StatisticsDataDTO getStatisticDataDTO()
    {
        return statisticsData;
    }

    @Override
    public void checkIfPositionsValid(List<Character> positions) {
        if (positions.size() != enigmaMachine.getRotorNumberInUse())
            throw new RuntimeException("You need to give position for each rotor.");
        tempSelectedInitPositions = new char[enigmaMachine.getRotorNumberInUse()];
        int i =enigmaMachine.getRotorNumberInUse()-1 ;
       for(Character chPos:positions)
       { char ch= Character.toUpperCase(chPos);
            if (enigmaMachine.getAlphabet().indexOf(ch) == -1)
                throw new RuntimeException("This position is not exist in the machine.");
            tempSelectedInitPositions[i--] = ch;

        }
    }

    @Override
    public String processDataInput(String dataInput) {

        resetProcessingTime();
        dataInput = dataInput.toUpperCase();
        StringBuilder output = new StringBuilder();

            for (int i = 0; i < dataInput.length(); i++) {
                output.append(processDataInput(dataInput.charAt(i)));
            }
            //System.out.println("output:" + output);
            addOutputStringToStatics(dataInput, output.toString());
            return output.toString();

    }

    @Override
    public void resetProcessingTime() {
        sumProcessingTime=0;
    }

    @Override
    public void resetCodePosition() {
        for (Rotor selectedRotor : selectedRotors) {
            selectedRotor.resetWindowPositionToInitialPosition();
        }
    }

    @Override
    public void addOutputStringToStatics(String input,String output) {
        statisticsData.addCipheredDataToStats(initialCodeFormat,input, output, sumProcessingTime);
        cipheredInputsAmount++;
    }

    private CodeFormat createCodeFormat(boolean isCalcDistanceFromInitWindow)
    {
        int rotorID;
        SimpleStringProperty rotorIDProperty = new SimpleStringProperty();
        int distanceToWindow;
        SimpleStringProperty distanceToWindowProperty=new SimpleStringProperty();
        char initPositionLetter;
        SimpleStringProperty initPositionProperty=new SimpleStringProperty();
        SimpleStringProperty reflectorProperty=new SimpleStringProperty();
        reflectorProperty.set(selectedReflector.getReflectorIdName());
        RotorInfoDTO[] rotorInfoArray=new RotorInfoDTO[enigmaMachine.getRotorNumberInUse()];
       // RotorInfoDTO.RotorInfoPropertyDTO[] rotorsIDProperty=new RotorInfoDTO.RotorInfoPropertyDTO[enigmaMachine.getRotorNumberInUse()];
        for (int i = 0; i < rotorInfoArray.length; i++) {
            rotorID=selectedRotors[i].getRotorID();
            rotorIDProperty.set(String.valueOf(rotorID));
            distanceToWindow=selectedRotors[i].calculateDistanceFromNotchToWindows(isCalcDistanceFromInitWindow);
            distanceToWindowProperty.set(String.valueOf(distanceToWindow));
            initPositionLetter=isCalcDistanceFromInitWindow? selectedPositions[i ]: selectedRotors[i].getLetterInWindowPosition();
            initPositionProperty.set(initPositionProperty.toString());
            rotorInfoArray[i]=new RotorInfoDTO(rotorID,distanceToWindow,initPositionLetter);
            //rotorsIDProperty[i]=new RotorInfoDTO.RotorInfoPropertyDTO(rotorIDProperty,distanceToWindowProperty,initPositionProperty);
        }
        //codeFormatProperty=new CodeFormatDTO.CodeFormatPropertyDTO(rotorsIDProperty,reflectorProperty,plugBoardPairsProperty);
        return new CodeFormat(rotorInfoArray,selectedReflector.getReflectorIdName(),plugBoardPairs);


    }
    @Override
    public CodeFormatDTO getCodeFormat(boolean isCalcDistanceFromInitWindow) {
        if (isCalcDistanceFromInitWindow)
            return initialCodeFormat;
        else
            return createCodeFormat(false);
    }

    @Override
    public void resetSelected() {
        selectedRotors = null;
        selectedPositions = null;
        selectedReflector = null;
        initialCodeFormat=null;
       // tempSelectedReflectorID=0;
      //  tempPlugBoardPairs=null;
        //tempSelectedInitPositions=null;
       // tempSelectedRotorsID=null;
        enigmaMachine.getPlugBoard().resetPlugBoardPairs();

    }


    @Override
    public void checkIfReflectorNumValid(String ReflectorNum) {
        int refNum;
        tempSelectedReflectorID=0;
        try {
            refNum = Integer.parseInt(ReflectorNum);
        } catch (Exception ex) {
            throw new RuntimeException("The number you entered isn't integer.");
        }
        if (!Reflector.isIdExist(refNum) || refNum > enigmaMachine.getReflectorsNumber())
            throw new RuntimeException("You need to choose one of the options 1-" + enigmaMachine.getReflectorsNumber());

        tempSelectedReflectorID=refNum;

    }

    @Override
    public void setReflector(String reflector)
    {
        tempSelectedReflectorID=Reflector.convertRomanIdToNumber(reflector);
    }



    @Override
    public void checkPlugBoardPairs(List<PlugboardPairDTO> plugBoardPairs)  {
        tempPlugBoardPairs=new ArrayList<>();
        if(plugBoardPairs.size()==0) {
            setMachineConfigurationByUser();
            return;
        }
        enigmaMachine.getPlugBoard().resetPlugBoardPairs();
        //withPlugBoardPairs=true;
        List<Character> alreadyExistLetter=new ArrayList<>();
        Set<Character> letterSet=new HashSet<>(plugBoardPairs.size());

        for (PlugboardPairDTO plugBoardPair : plugBoardPairs) {
            if (letterSet.contains(plugBoardPair.getFirstLetter()))
                alreadyExistLetter.add(plugBoardPair.getFirstLetter());
            if (letterSet.contains(plugBoardPair.getSecondLetter()))
                alreadyExistLetter.add(plugBoardPair.getSecondLetter());
            else {
                letterSet.add(plugBoardPair.getFirstLetter());
                letterSet.add(plugBoardPair.getSecondLetter());

            }
        }
        if(alreadyExistLetter.size()!=0)
            throw new RuntimeException("In plugBoard pairs the letters:\n"+alreadyExistLetter+ " appears more then once.");


        tempPlugBoardPairs.addAll(plugBoardPairs);

        setMachineConfigurationByUser();//user success to choose all machine configuration ,move to new setup
    }



    public void setMachineConfigurationByUser()
    {
        resetSelected();

        selectedPositions=tempSelectedInitPositions;
        selectedRotors=new Rotor[enigmaMachine.getRotorNumberInUse()];
        for (int i = 0; i <enigmaMachine.getRotorNumberInUse() ; i++) {
            int rotorId=tempSelectedRotorsID.get(i);
            selectedRotors[i] = enigmaMachine.getRotorById(rotorId);
            selectedRotors[i].setInitialWindowPosition(selectedPositions[i]);
        }
        selectedReflector=enigmaMachine.getReflectorById(tempSelectedReflectorID);
        plugBoardPairs=tempPlugBoardPairs;
      for(PlugboardPairDTO pair:tempPlugBoardPairs)
      {
          enigmaMachine.getPlugBoard().addMappedInputOutput(pair.getFirstLetter(),pair.getSecondLetter());
      }

        setInitialCode();

    }

    @Override
    public void  saveMachineStateToFile(String filePathNoExtension) {
        filePathNoExtension=filePathNoExtension.replaceAll("\"","");//for case user enter with " "
        filePathNoExtension+=stateMachineFileExtension;
        try (ObjectOutputStream out =
                     new ObjectOutputStream(
                             Files.newOutputStream(Paths.get(filePathNoExtension)))) {
            out.writeObject(this);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }



    public static EnigmaEngine loadMachineStateFromFile(String filePathNoExtension) {
        filePathNoExtension = filePathNoExtension.replaceAll("\"", "");//for case user enter with " "
        filePathNoExtension += stateMachineFileExtension;
        File savedStateFile = new File(filePathNoExtension);
        if (!savedStateFile.exists())
            throw new RuntimeException("This file doesn't exists. PLease enter valid file path");

        try (ObjectInputStream in =
                     new ObjectInputStream(
                             Files.newInputStream(Paths.get(filePathNoExtension)))) {
                   return (EnigmaEngine) in.readObject();

        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public void setCodeAutomatically() {
        setRandomRotors();
        setRandomReflector();
        setRandomPositions();
       // setRandomPlugboardPairs();
        setInitialCode();
    }

    private void setInitialCode() {
        initialCodeFormat = createCodeFormat(true);
        statisticsData.addNewCodeToListIfAbsent(initialCodeFormat);
    }

    @Override
    public boolean getWithPlugBoardPairs()
    {
        return plugBoardPairs!=null&&plugBoardPairs.size()!=0;
    }

    private void setRandomRotors() {
        Random random = new Random();
        int rotorNum;
        Set<Integer> alreadyUsed=new HashSet<>();
        selectedRotors = new Rotor[enigmaMachine.getRotorNumberInUse()];
        for (int i = 0; i < enigmaMachine.getRotorNumberInUse(); i++) {
            selectedRotors[i] = null;
        }
        for (int i = 0; i < enigmaMachine.getRotorNumberInUse(); i++) {
            do {
                rotorNum=random.nextInt(enigmaMachine.getNumberOfRotors()) + 1;
            }
            while (alreadyUsed.contains(rotorNum));
            alreadyUsed.add(rotorNum);
            selectedRotors[i] = enigmaMachine.getRotorById(rotorNum);
        }
    }

    private void setRandomReflector() {
        int reflectorNum=ThreadLocalRandom.current().nextInt(enigmaMachine.getReflectorsNumber())+1;
        selectedReflector = enigmaMachine.getReflectorById(reflectorNum);
    }

    private void setRandomPositions() {
        selectedPositions = new char[selectedRotors.length];
        for (int i = 0; i < selectedRotors.length; i++) {
            selectedPositions[i] = getRandomCharacter(enigmaMachine.getAlphabet());
            selectedRotors[i].setInitialWindowPosition(selectedPositions[i]);
        }

    }

    private void setRandomPlugboardPairs() {
        enigmaMachine.getPlugBoard().resetPlugBoardPairs();
        Random random = new Random();
        boolean withPlugBoardPairs = random.nextBoolean();
        plugBoardPairs=new ArrayList<>();
        boolean res;
        if (withPlugBoardPairs) {

            String alphabet=enigmaMachine.getAlphabet();

            int alphabetSize=alphabet.length();
            Set<Character> alreadyExist=new HashSet<>(alphabet.length());
            int numOfPairs = random.nextInt(alphabet.length() / 2) + 1;
            char input,output;
            for (int i = 0; i < numOfPairs; i++) {
                 res = false;
                while (!res) {
                    try {
                        do{
                            input = alphabet.charAt(random.nextInt(alphabetSize));
                        } while(alreadyExist.contains(input));
                        alreadyExist.add(input);
                       do {
                           output = alphabet.charAt(random.nextInt(alphabetSize));
                       }while(alreadyExist.contains(output));

                                alreadyExist.add(output);
                        enigmaMachine.getPlugBoard().addMappedInputOutput(input, output);
                        plugBoardPairs.add(new PlugboardPairDTO(input,output));
                        res = true;
                    } catch (RuntimeException ex) {
                        throw new RuntimeException(ex);
                    }

                }
            }

        }
    }

        private char getRandomCharacter ( String alphabet){
            return alphabet.charAt(ThreadLocalRandom.current().nextInt(alphabet.length()));
        }





        private void copyAllData (CTEEnigma eng){
            String alphabet = eng.getCTEMachine().getABC().trim().toUpperCase();
            if (alphabet.length() % 2 != 0)
                throw new RuntimeException("The number of letters need to be even.");

            if (eng.getCTEMachine().getRotorsCount() < 2)
                throw new RuntimeException("The number of rotors need to be bigger or equal to 2.");
            if (eng.getCTEMachine().getRotorsCount() > eng.getCTEMachine().getCTERotors().getCTERotor().size())
                throw new RuntimeException("The number of rotors that used is greater than the number of rotors given with the machine.");

            EnigmaMachine tempEnigmaMachine = new EnigmaMachine();
            tempEnigmaMachine.setAlphabet(eng.getCTEMachine().getABC());
            tempEnigmaMachine.setRotorsInUse(eng.getCTEMachine().getRotorsCount());
            tempEnigmaMachine.setReflectors(eng.getCTEMachine().getCTEReflectors().getCTEReflector());
            tempEnigmaMachine.setRotors(eng.getCTEMachine().getCTERotors().getCTERotor());

            initialCodeFormat=null;

            int[] rotorsArrayId = copyRotorsID(eng.getCTEMachine().getCTERotors().getCTERotor());
            //agentAmount=eng.getCTEDecipher().getAgents();
            List<Character> excludeChars=copyExcludeChars(eng.getCTEDecipher().getCTEDictionary().getExcludeChars());

//            if(agentAmount<1||agentAmount>50)
//                throw  new RuntimeException("Invalid number of agents "+agentAmount+" number need to between 2 to 50");

            setBattlefieldData(eng.getCTEBattlefield());
            dictionary=new Dictionary(eng.getCTEDecipher().getCTEDictionary().getWords(),excludeChars,tempEnigmaMachine.getAlphabet());
            machineData = new MachineDataDTO( eng.getCTEMachine().getRotorsCount(),
                                             rotorsArrayId,
                                            tempEnigmaMachine.getReflectorIDList(),
                                            tempEnigmaMachine.getAlphabet(), dictionary.getWordsSet());
            enigmaMachine= tempEnigmaMachine;

            resetAllData();
        }
        private void setBattlefieldData(CTEBattlefield cteBattlefield)
        {
            int alliesAmount=cteBattlefield.getAllies();
            String level=cteBattlefield.getLevel().toUpperCase();
            String battlefieldName=cteBattlefield.getBattleName();
            GameLevel gameLevel;
            if(loadedBattlefieldName.contains(battlefieldName))
                throw new RuntimeException(battlefieldName+" already loaded in server!");
            if(alliesAmount<1)
                throw new RuntimeException("invalid allies amount: "+alliesAmount);
            try {
                 gameLevel = GameLevel.valueOf(level);

            }catch (IllegalArgumentException e)
            {
                throw new RuntimeException("the "+level+" isn't valid game level");
            }
            battlefieldDataDTO=new BattlefieldDataDTO(battlefieldName,alliesAmount,gameLevel.toString());
        }

        private List<Character> copyExcludeChars(String excludeChars) {
        List<Character> excludeCharsList= new ArrayList<>();
        for (int i = 0; i < excludeChars.length(); i++) {
            excludeCharsList.add(excludeChars.charAt(i));
        }
        return excludeCharsList;
    }



    private int[] copyRotorsID (List < CTERotor > rotorsArray)
        {
            int[] rotorsID = new int[rotorsArray.size()];
            for (int i = 0; i < rotorsArray.size(); i++) {
                rotorsID[i] = rotorsArray.get(i).getId();
            }
            return rotorsID;
        }

    @Override
    public char processDataInput(char charInput)  {
        long startTime = System.nanoTime();
        charInput = Character.toUpperCase(charInput);
        // System.out.println("char is:"+charInput);

        boolean advanceNextRotor = true;//first right rotor always advance every typing of letter
        //the row input after moving in plug board

            int currentRow = enigmaMachine.getKeyboard().getMappedOutput(charInput);
            //move input flow from right rotor to left rotors
            for (Rotor selectedRotor : selectedRotors) {
                advanceNextRotor = selectedRotor.forwardWindowPosition(advanceNextRotor);
                currentRow = selectedRotor.getOutputMapIndex(currentRow, false);
            }
            currentRow = selectedReflector.getMappedOutput(currentRow);
//        System.out.format("Reflect:%d->%d\n", rotorOutput.getOutputIndex(),currentRow);
            //input flow go back from left rotor to right rotor after reflector
            for (int j = selectedRotors.length - 1; j >= 0; j--) {
                currentRow = selectedRotors[j].getOutputMapIndex(currentRow, true);
            }
            sumProcessingTime += System.nanoTime() - startTime;

            return enigmaMachine.getKeyboard().getLetterFromRowNumber(currentRow);




    }




    public int getCipheredInputsAmount()
    {
        return cipheredInputsAmount;
    }
    @Override
    public String toString() {
        return "MenuEngine{" +
                ", \nmachineData=" + machineData +
                ", \nplugBoardPairs=" + plugBoardPairs +
                ", \nselectedRotors=" + Arrays.toString(selectedRotors) +
                ", \nstatisticsData=" + statisticsData +
                ", \nselectedPositions=" + Arrays.toString(selectedPositions) +
                ", \nselectedReflector=" + selectedReflector +
                ", \nwithPlugBoardPairs=" + getWithPlugBoardPairs() +
                ", \ncipheredInputs=" + cipheredInputsAmount +
                '}';
    }

@Override
    public BattlefieldDataDTO getBattlefieldDataDTO() {
        return battlefieldDataDTO;
    }
}
