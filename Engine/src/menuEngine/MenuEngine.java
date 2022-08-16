package menuEngine;

import dtoObjects.*;
import enigmaMachine.enigmaMachine;
import enigmaMachine.parts.Reflector;
import enigmaMachine.parts.Rotor;
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

public class MenuEngine implements Engine , Serializable {

    private final static String JAXB_XML_PACKAGE_NAME = "jaxb";
    private enigmaMachine enigmaMachine;
    private MachineDataDTO machineData;
    private SelectedConfigurationDTO selectedConfigurationDTO;
    private List<PlugboardPairDTO> plugBoardPairs;
    private Rotor[] selectedRotors;
    private StatisticsData statisticsData;
    private char[] selectedPositions;
    private Reflector selectedReflector = null;
    private int cipheredInputs;
    private CodeFormatDTO initialCodeFormat =null;
    boolean withPlugBoardPairs;
    @Override
    public boolean isMachineLoaded()
    {
        return enigmaMachine!=null;
    }

    public MenuEngine() {
        enigmaMachine = null;
        statisticsData=new StatisticsData();
        selectedConfigurationDTO =new SelectedConfigurationDTO();
       // cipheredInputs=0;
    }

    public void resetAllData()
    {
        selectedConfigurationDTO=new SelectedConfigurationDTO();
        plugBoardPairs=new ArrayList<>();
        statisticsData=new StatisticsData();
        selectedRotors=null;
        selectedPositions=null;
        selectedReflector=null;
        cipheredInputs=0;
    }


    @Override
    public MachineDataDTO getMachineData() {

        return machineData;
    }

    @Override
    public String getAlphabetString() {
        return enigmaMachine.getAlphabet();
    }

    @Override
    public SelectedConfigurationDTO getSelectedData() {
        createSelectedDataObj(false);
        return selectedConfigurationDTO;
    }

    @Override
    public void LoadXMLFile(String filePath) {
        filePath=filePath.replaceAll("\"","");//for case user enter with " "

        File file = new File(filePath);

        if (!(filePath.toLowerCase().endsWith(".xml")))
            throw new RuntimeException("The file you entered isn't XML file.");
        else if (!file.exists())
            throw new RuntimeException("The file you entered isn't exists.");

        try {
            InputStream inputStream = new FileInputStream(filePath);
            JAXBContext jc = JAXBContext.newInstance(JAXB_XML_PACKAGE_NAME);
            Unmarshaller u = jc.createUnmarshaller();
            CTEEnigma eng = (CTEEnigma) u.unmarshal(inputStream);
            copyAllData(eng);

        } catch (JAXBException | FileNotFoundException e) {
            e.printStackTrace();
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public void checkIfRotorsValid(String rotors) {
        List<String> arrayString = Arrays.asList(rotors.split(","));
        selectedRotors = new Rotor[enigmaMachine.getRotorNumberInUse()];
        for (int i = 0; i < enigmaMachine.getRotorNumberInUse(); i++) {
            selectedRotors[i] = null;
        }
        int i = 0;
        int rotorNum;
        if (arrayString.size() != enigmaMachine.getRotorNumberInUse())
            throw new RuntimeException("You need to enter " + enigmaMachine.getRotorNumberInUse() + " rotors with comma between them.");

        for (int j = arrayString.size() - 1; j >= 0; j--) {
            try {
                rotorNum = Integer.parseInt(arrayString.get(j));
            } catch (Exception ex) {
                throw new RuntimeException("The number you entered isn't integer.");
            }
            if (rotorNum > enigmaMachine.getNumberOfRotors() || rotorNum < 1)
                throw new RuntimeException("There is no such rotors.");
            if (findRotorByIdInSelectedRotors(rotorNum) != null)
                throw new RuntimeException("You select the same rotor twice.");
            selectedRotors[i++] = enigmaMachine.getRotorById(rotorNum);
        }

//        enigmaMachine.setSelectedRotors(selectedRotors);
    }



    @Override
    public StatisticsDataDTO getStatisticDataDTO()
    {
        return statisticsData;
    }

    @Override
    public void checkIfPositionsValid(String positions) {
        positions = positions.toUpperCase();
        char[] positionsList = new char[enigmaMachine.getRotorNumberInUse()];
        int i = enigmaMachine.getRotorNumberInUse()-1;
        if (positions.length() != enigmaMachine.getRotorNumberInUse())
            throw new RuntimeException("You need to give position for each rotor.");
        for (char ch : positions.toCharArray()) {
            if (enigmaMachine.getAlphabet().indexOf(ch) == -1)
                throw new RuntimeException("This position is not exist in the machine.");
            positionsList[i] = ch;
            i--;
        }
        selectedPositions = positionsList;
        for (int j = 0; j < selectedRotors.length; j++) {
            selectedRotors[j].setInitialWindowPosition(selectedPositions[j]);
        }
       // enigmaMachine.setSelectedPositions(positionsList);
    }

    @Override
    public String cipherData(String dataInput) {
        int currentRow;
        long startTime=System.nanoTime();
        dataInput = dataInput.toUpperCase();
        StringBuilder output = new StringBuilder();

        for (int i = 0; i < dataInput.length(); i++) {
            boolean advanceNextRotor = true;//first right rotor always advance every typing of letter
            //the row input after moving in plug board
            currentRow = enigmaMachine.getKeyboard().getMappedOutput(dataInput.charAt(i));
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
//        System.out.println("=====");
            output.append(enigmaMachine.getKeyboard().getLetterFromRowNumber(currentRow));
        }
        //System.out.println("output:" + output);
        long endTime=System.nanoTime();
        statisticsData.addCipheredDataToStats(getCodeFormat(true),dataInput, output.toString(), endTime-startTime);
        cipheredInputs++;
        return output.toString();
    }
    @Override
    public int getNumberOfRotorInSystem()
    {
        return enigmaMachine.getNumberOfRotorsInMachine();
    }
    @Override
    public void resetCodePosition() {
        for (Rotor selectedRotor : selectedRotors) {
            selectedRotor.resetWindowPositionToInitialPosition();
        }
    }


    private CodeFormatDTO createCodeFormat(boolean isCalcDistanceFromInitWindow)
    {
        int rotorID;
        int distanceToWindow;
        char initPositionLetter;
        RotorInfoDTO[] rotorInfoArray=new RotorInfoDTO[enigmaMachine.getRotorNumberInUse()];
        for (int i = 0; i < rotorInfoArray.length; i++) {
            rotorID=selectedRotors[i].getRotorID();
            distanceToWindow=selectedRotors[i].calculateDistanceFromNotchToWindows(isCalcDistanceFromInitWindow);
            initPositionLetter=selectedPositions[i];
            rotorInfoArray[i]=new RotorInfoDTO(rotorID,distanceToWindow,initPositionLetter);

        }
        return new CodeFormatDTO(rotorInfoArray,selectedReflector.getReflectorIdName(),plugBoardPairs);


    }
    @Override
    public CodeFormatDTO getCodeFormat(boolean isCalcDistanceFromInitWindow) {
        if (isCalcDistanceFromInitWindow)
            return initialCodeFormat;
        else
            return createCodeFormat(false);

//            int[] selectedRotorsArray= selectedConfigurationDTO.getSelectedRotorsID();
//            char[] selectedPositions= selectedConfigurationDTO.getSelectedPositions();
//            int[] notchArray;
//
//            if(selectedRotorsArray==null)//if user only start the program and not select any configuration
//                return "";
//
//            StringBuilder codeFormat=new StringBuilder();
//            codeFormat.append('<');
//            notchArray= isSelectedData? selectedConfigurationDTO.getNotchPositions() : setNotchPositions();
//
//            for(int i=selectedRotorsArray.length-1;i>0;i--)
//            {
//                codeFormat.append(selectedRotorsArray[i]);
//                if(isHistory)
//                    codeFormat.append(",");
//                else
//                    codeFormat.append("(").append(notchArray[i]).append(")").append(",");
//            }
//            codeFormat.append(selectedRotorsArray[0]);
//            if(isHistory)
//                codeFormat.append(",");
//            else
//                codeFormat.append("(").append(notchArray[0]).append(")");
//
//            for(int i=selectedPositions.length-1;i>=0;i--) {
//                codeFormat.append(selectedPositions[i]);
//            }
//           codeFormat.append(">");
//            codeFormat.append(String.format("<%s>", selectedConfigurationDTO.getSelectedReflectorID()));
//
//            if(withPlugBoardPairs)
//            {
//                List<String> pairs= selectedConfigurationDTO.getPlugBoardPairs();
//                codeFormat.append('<');
//                for (int i = 0; i < pairs.size()-1; i++)
//                    codeFormat.append(String.format("%c|%c,", pairs.get(i).charAt(0), pairs.get(i).charAt(1)));
//
//                codeFormat.append(String.format("%c|%c>",pairs.get(pairs.size()-1).charAt(0),pairs.get(pairs.size()-1).charAt(1)));
//
//            }
//            return codeFormat.toString();

    }

    @Override
    public void resetSelected() {
        selectedRotors = null;
        selectedPositions = null;
        selectedReflector = null;
        enigmaMachine.getPlugBoard().resetPlugBoardPairs();
        createSelectedDataObj(true);
    }

    @Override
    public List<String> getReflectorIdList() {
        return enigmaMachine.getReflectorIDList();
    }

    @Override
    public void checkIfReflectorNumValid(String ReflectorNum) {
        int refNum;
        try {
            refNum = Integer.parseInt(ReflectorNum);
        } catch (Exception ex) {
            throw new RuntimeException("The number you entered isn't integer.");
        }
        if (!Reflector.isIdExist(refNum) || refNum > enigmaMachine.getReflectorsNumber())
            throw new RuntimeException("You need to choose one of the options 1-" + enigmaMachine.getReflectorsNumber());

        selectedReflector = enigmaMachine.getReflectorById(refNum);
        //enigmaMachine.setSelectedReflector(selectedReflector);
    }



    @Override

    public void checkPlugBoardPairs(String pairs){


        plugBoardPairs=new ArrayList<>();
        if(pairs.length()==0)
        {
            withPlugBoardPairs=false;
            return;
        }


        if(pairs.length()%2!=0)
            throw new RuntimeException("There is a character that has no pair.must be even number in input string.\nPlease correct this.");


        withPlugBoardPairs=true;
        List<String> alreadyExistLetter=new ArrayList<>();
        Set<Character> letterSet=new HashSet<>(pairs.length());
        for(int i=0;i<pairs.length();i++) {
            if (enigmaMachine.getAlphabet().indexOf(pairs.charAt(i)) == -1 )
                throw new RuntimeException("Pair: " + pairs.charAt(i) + " doesn't exist in the machine alphabet.");
            if(letterSet.contains(pairs.charAt(i)))
                alreadyExistLetter.add(pairs.charAt(i)+"\n");
            else
                letterSet.add(pairs.charAt(i));
        }
        if(alreadyExistLetter.size()!=0)
            throw new Exception("the letters:\n"+alreadyExistLetter+ " appears more then once.");

        for(int i=0;i<pairs.length();i+=2)
        {
            plugBoardPairs.add(new PlugboardPairDTO(pairs.charAt(i),pairs.charAt(i+1)));
            enigmaMachine.getPlugBoard().addMappedInputOutput(pairs.charAt(i),pairs.charAt(i+1));

        }



    }
    @Override
    public void  saveMachineStateToFile(String filePathNoExtension) {
        filePathNoExtension=filePathNoExtension.replaceAll("\"","");//for case user enter with " "
        filePathNoExtension+=".bat";
        try (ObjectOutputStream out =
                     new ObjectOutputStream(
                             Files.newOutputStream(Paths.get(filePathNoExtension)))) {
            out.writeObject(this);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public int checkPlugBoardNum(String plugBoardNum) {
        return 0;
    }

    public static MenuEngine loadMachineStateFromFile(String filePathNoExtension) {
    filePathNoExtension=filePathNoExtension.replaceAll("\"","");//for case user enter with " "
    filePathNoExtension+=".bat";
    File savedStateFile = new File(filePathNoExtension);
    if(!savedStateFile.exists())
        throw new RuntimeException("This file doesn't exists. PLease enter valid file path");

    try (ObjectInputStream in =
                     new ObjectInputStream(
                             Files.newInputStream(Paths.get(filePathNoExtension)))) {
            return (MenuEngine) in.readObject();


        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public void saveMachineStateToFile(String filePathNoExtension) {
        filePathNoExtension=filePathNoExtension.replaceAll("\"","");//for case user enter with " "
        filePathNoExtension+=".bat";
        try (ObjectOutputStream out =
                     new ObjectOutputStream(
                             Files.newOutputStream(Paths.get(filePathNoExtension)))) {
            out.writeObject(this);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static MenuEngine loadMachineStateFromFile(String filePathNoExtension) {
    filePathNoExtension=filePathNoExtension.replaceAll("\"","");//for case user enter with " "
    filePathNoExtension+=".bat";
    File savedStateFile = new File(filePathNoExtension);
    if(!savedStateFile.exists())
        throw new RuntimeException("This file doesn't exists. PLease enter valid file path");

    try (ObjectInputStream in =
                     new ObjectInputStream(
                             Files.newInputStream(Paths.get(filePathNoExtension)))) {
            return (MenuEngine) in.readObject();


        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void setCodeAutomatically() {
        setRandomRotors();
        setRandomReflector();
        setRandomPositions();
        setRandomPlugboardPairs();
        initialCodeFormat =createCodeFormat(true);
        createSelectedDataObj(true);
    }

    @Override
    public boolean getWithPlugBoardPairs()
    {
        return withPlugBoardPairs;
    }

    private void setRandomRotors() {
        Random random = new Random();
        int rotorNum;
        selectedRotors = new Rotor[enigmaMachine.getRotorNumberInUse()];
        for (int i = 0; i < enigmaMachine.getRotorNumberInUse(); i++) {
            selectedRotors[i] = null;
        }
        for (int i = 0; i < enigmaMachine.getRotorNumberInUse(); i++) {
            do {
                rotorNum=random.nextInt(enigmaMachine.getNumberOfRotors()) + 1;
            }
            while (rotorNum > enigmaMachine.getNumberOfRotors() ||
                    findRotorByIdInSelectedRotors(rotorNum) != null);
            selectedRotors[i] = enigmaMachine.getRotorById(rotorNum);
        }
    }

    private void setRandomReflector() {
        int reflectorNum=ThreadLocalRandom.current().nextInt(enigmaMachine.getReflectorsNumber())+1;
        selectedReflector = enigmaMachine.getReflectorById(reflectorNum);
    }

//    private static Reflector setRandomReflector(Reflector[] arr) {
//        return arr[ThreadLocalRandom.current().nextInt(arr.length)];
//    }

    private void setRandomPositions() {
        selectedPositions = new char[selectedRotors.length];
        char[] alphabetArray = new char[enigmaMachine.getAlphabet().length()];
        for (int i = 0; i < enigmaMachine.getAlphabet().length(); i++) {
            alphabetArray[i] = enigmaMachine.getAlphabet().charAt(i);
        }
        for (int i = 0; i < selectedRotors.length; i++) {
            selectedPositions[i] = getRandomCharacter(alphabetArray);
        }

        for (int j = 0; j < selectedRotors.length; j++) {
            selectedRotors[j].setInitialWindowPosition(selectedPositions[j]);
        }
    }

    private void setRandomPlugboardPairs() {
        Random random = new Random();
        withPlugBoardPairs = random.nextBoolean();
        plugBoardPairs=new ArrayList<>();
        boolean res;
        if (withPlugBoardPairs) {
            List<Character> charList = new ArrayList<>();
            for (int i = 0; i < enigmaMachine.getAlphabet().length(); i++) {
                charList.add(enigmaMachine.getAlphabet().charAt(i));
            }
            int numOfPairs = random.nextInt(enigmaMachine.getAlphabet().length() / 2) + 1;
            for (int i = 0; i < numOfPairs; i++) {
                 res = false;
                while (!res) {
                    try {
                        char input = charList.get(random.nextInt(charList.size()));
                        charList.remove(charList.indexOf(input));
                        char output = charList.get(random.nextInt(charList.size()));
                        while (!charList.contains(output))
                            output = charList.get(random.nextInt(charList.size()));
                        charList.remove(charList.indexOf(output));
                        enigmaMachine.getPlugBoard().addMappedInputOutput(input, output);
                        plugBoardPairs.add(new PlugboardPairDTO(input,output));
                        res = true;
                    } catch (Exception ex) {
                        break;
                    }

                }
            }

        }
    }

        private char getRandomCharacter ( char[] arr){
            return arr[ThreadLocalRandom.current().nextInt(arr.length)];
        }


        private Rotor findRotorByIdInSelectedRotors ( int rotorNum)
        {
            for (Rotor rotor : selectedRotors) {
                if (rotor != null && rotor.getRotorID() == rotorNum)
                    return rotor;
            }
            return null;
        }


        private void createSelectedDataObj ( boolean alreadyExists)
        {
            if (!alreadyExists) {
                int[] notchPositions = setNotchPositions();
                int[] rotorsID = copySelectedRotorsID(selectedRotors);
                selectedConfigurationDTO = new SelectedConfigurationDTO(selectedPositions, selectedReflector.getReflectorIdName(),
                        rotorsID, plugBoardPairs,notchPositions);
            } else {
                selectedConfigurationDTO = new SelectedConfigurationDTO();
            }

        }

        private int[] copySelectedRotorsID (Rotor[] selectedRotors)
        {
            int[] selectedRotorsID = new int[selectedRotors.length];
            for (int i = selectedRotorsID.length-1; i >= 0 ; i--) {
                selectedRotorsID[i] = selectedRotors[i].getRotorID();
            }
            return selectedRotorsID;
        }

        private void copyAllData (CTEEnigma eng){
            String alphabet = eng.getCTEMachine().getABC().replaceAll("\n", "");
            alphabet = alphabet.replaceAll("\t", "");

            if (alphabet.length() % 2 != 0)
                throw new RuntimeException("The number of letters need to be even.\nPlease correct this.");

            if (eng.getCTEMachine().getRotorsCount() < 2)
                throw new RuntimeException("The number of rotors need to be bigger or equal to 2.\nPlease correct this.");
            if (eng.getCTEMachine().getRotorsCount() > eng.getCTEMachine().getCTERotors().getCTERotor().size())
                throw new RuntimeException("The number of rotors that used is greater than the number of rotors given with the machine.\nPlease correct this.");

            enigmaMachine=new enigmaMachine();
            enigmaMachine.setAlphabet(eng.getCTEMachine().getABC());
            enigmaMachine.setRotorsInUse(eng.getCTEMachine().getRotorsCount());
            enigmaMachine.setReflectors(eng.getCTEMachine().getCTEReflectors().getCTEReflector());
            enigmaMachine.setRotors(eng.getCTEMachine().getCTERotors().getCTERotor());
            int[] rotorsArrayId = copyRotorsID(eng.getCTEMachine().getCTERotors().getCTERotor());
            int[] notchArray = copyNotchArray(eng.getCTEMachine().getCTERotors().getCTERotor());
            machineData = new MachineDataDTO(eng.getCTEMachine().getCTEReflectors().getCTEReflector().size(),
                    eng.getCTEMachine().getRotorsCount(), rotorsArrayId, notchArray);
        }



        private int[] setNotchPositions()
        {
            int[] notchArray=new int[selectedPositions.length];
            for (int i = 0; i < selectedPositions.length; i++) {
                notchArray[i]=selectedRotors[i].calculateDistanceFromNotchToWindows(false);
            }
            return notchArray;
        }

        private int[] copyRotorsID (List < CTERotor > rotorsArray)
        {
            int[] rotorsID = new int[rotorsArray.size()];
            for (int i = 0; i < rotorsArray.size(); i++) {
                rotorsID[i] = rotorsArray.get(i).getId();
            }
            return rotorsID;
        }

        private int[] copyNotchArray (List < CTERotor > rotorsArray)
        {
            int[] notchNumbers = new int[rotorsArray.size()];
            for (int i = 0; i < rotorsArray.size(); i++) {
                notchNumbers[i] = rotorsArray.get(i).getNotch();
            }
            return notchNumbers;
        }

    public int getCipheredInputs()
    {
        return cipheredInputs;
    }
    @Override
    public String toString() {
        return "MenuEngine{" +
                ", \nmachineData=" + machineData +
                ", \nselectedConfigurationDTO=" + selectedConfigurationDTO +
                ", \nplugBoardPairs=" + plugBoardPairs +
                ", \nselectedRotors=" + Arrays.toString(selectedRotors) +
                ", \nstatisticsData=" + statisticsData +
                ", \nselectedPositions=" + Arrays.toString(selectedPositions) +
                ", \nselectedReflector=" + selectedReflector +
                ", \nwithPlugBoardPairs=" + withPlugBoardPairs +
                ", \ncipheredInputs=" + cipheredInputs +
                '}';
    }

}
