package enigmaMachine.parts;


import java.io.Serializable;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;


public class Rotor implements Serializable {

    //right side of the rotor
    private Map<Character,Integer> letter2IndexRightSide =null;
    private char[] index2letterRightSide =null;
    //left side of the rotor

    private Map<Character,Integer> letter2IndexLeftSide =null;
    private char[] index2letterLeftSide =null;

    private final int letterSize;
    private int currentMapSize=0;
    private int windowPosition=0;
    private final int notchPosition;
    private final int rotorID;
    private int initialWindowPosition;
    private final String alphabet;
    private final boolean debugMode;
       public Rotor(int letterSize, int notch, int id, String alphabet,boolean debugMode) {
        this.letterSize = letterSize;
        this.notchPosition = notch-1;
        this.rotorID = id;
        this.debugMode = debugMode;
        this.alphabet=alphabet;
        initRotorArrays();
    }
    public Rotor(int letterSize, int notch, int id,String alphabet) {
        this(letterSize, notch, id,alphabet, false);
    }

    public int getRotorID() {
        return rotorID;
    }

    public void setInitialWindowPosition(char letter) {
        initialWindowPosition = letter2IndexRightSide.get(letter);
        resetWindowPositionToInitialPosition();

    }

private void initRotorArrays() {

        index2letterRightSide = new char[letterSize];
        index2letterLeftSide = new char[letterSize];
        letter2IndexLeftSide = new HashMap<>(letterSize);
        letter2IndexRightSide = new HashMap<>(letterSize);
        for (int i = 0; i < letterSize; i++) {
            index2letterLeftSide[i] = index2letterRightSide[i] = 0;
        }
    }
    public void resetWindowPositionToInitialPosition() {
        windowPosition = initialWindowPosition;
    }


    public void addMapLetterToRotor(char leftLetter, char rightLetter) {
           leftLetter=Character.toUpperCase(leftLetter);
           rightLetter=Character.toUpperCase(rightLetter);

        if (currentMapSize > letterSize)
            throw new RuntimeException("overflow letter size,too many mapped letter!");
        if(alphabet.indexOf(leftLetter)==-1)
            throw new RuntimeException("the letter "+leftLetter+ " don't exist in the alphabet.");
        if(alphabet.indexOf(rightLetter)==-1)
            throw new RuntimeException("the letter "+rightLetter+ " don't exist in the alphabet.");
        if (letter2IndexLeftSide.containsKey(leftLetter))
            throw new RuntimeException("the letter "+ leftLetter+ " is already in roter table with "+ index2letterRightSide[letter2IndexLeftSide.get(leftLetter)]);
        if (letter2IndexRightSide.containsKey(rightLetter))
            throw new RuntimeException("the letter "+ rightLetter+ " is already in roter table with "+  index2letterLeftSide[letter2IndexRightSide.get(rightLetter)]);
        index2letterRightSide[currentMapSize] = rightLetter;
        index2letterLeftSide[currentMapSize] = leftLetter;

        letter2IndexLeftSide.put(leftLetter, currentMapSize);
        letter2IndexRightSide.put(rightLetter, currentMapSize);
        currentMapSize++;
    }



   private int calcIndexRotorTable(int index, boolean isInnerTableIndex) {

        int value;
        if (isInnerTableIndex)
            value = index - windowPosition;//the index is inner index table
        else
            value = index + windowPosition;//outer index that came from outer source
        //(a % b + b) % b get the correct answer also for (negative number)%(positive number)
        return (value % letterSize + letterSize) % letterSize;


    }
     public int calculateDistanceFromNotchToWindows(boolean isFromInitialWindow)
     {
         int distance=isFromInitialWindow?(notchPosition-initialWindowPosition):(notchPosition-windowPosition);
         return (distance % letterSize + letterSize) % letterSize;
     }
    /**
     * advance the window if window position arrive notch position [(window position)==(notch position)] previous rotor
     * @param previousNotchRotorArriveWindow true make the rotor advance the window position
     * @return boolean if you need to advance next rotor from right
     */
    public boolean forwardWindowPosition(boolean previousNotchRotorArriveWindow) {
        if (previousNotchRotorArriveWindow) {
            windowPosition++;
            windowPosition = windowPosition % letterSize;
        }

        return  notchPosition == windowPosition;
    }
    /**
     * map input line to output line on rotor table
     * @param inputRowRotorTable the input line number of the rotor table
     * @param isInputFromLeft the input to rotor is from right(false) or from left(true)
     * @return output row from rotor table that determined by input letter in table
     */
    public Integer getOutputMapIndex(int inputRowRotorTable, boolean isInputFromLeft) {

        int innerTableIndex = calcIndexRotorTable(inputRowRotorTable, false);
        int otherSideRelativIndex;

       // if (debugMode)
         //   System.out.format("Rotor ID:%d,letter:%c\n", this.rotorID, index2letterRightSide[innerTableIndex]);
        char inputLetter;
        if (isInputFromLeft) {

            inputLetter = index2letterLeftSide[innerTableIndex];//translate the input index to letter in left side
            otherSideRelativIndex = calcIndexRotorTable(letter2IndexRightSide.get(inputLetter), true);
        } else {
            inputLetter = index2letterRightSide[innerTableIndex];//translate the input index to letter in right side
            otherSideRelativIndex = calcIndexRotorTable(letter2IndexLeftSide.get(inputLetter), true);
        }
       // if (debugMode)
         //   System.out.format("Rotor ID:%d,input letter:%c,input flow:%d -> %d\n", this.rotorID, inputLetter, inputRowRotorTable, otherSideRelativIndex);
        return otherSideRelativIndex;
    }

    public boolean checkIfAllLetterMapped() {
        return letter2IndexLeftSide.size() == letterSize && letter2IndexRightSide.size() == letterSize;

    }
    public char getLetterInWindowPosition()
    {
        return index2letterRightSide[windowPosition];
    }
    public void printRotorTableDebugModeOnly()
    {
        if(debugMode)
        {
         //   System.out.println("Rotor id:"+rotorID);
            for (int i = 0; i < letterSize; i++) {

                System.out.println(index2letterLeftSide[i]+" "+ index2letterRightSide[i]);


            }
        }
    }

    @Override
    public String toString() {
        return "Rotor{" +

                "letter2IndexRightSide=" + letter2IndexRightSide +
                ", index2letterRightSide=" + Arrays.toString(index2letterRightSide) +
                ", letter2IndexLeftSide=" + letter2IndexLeftSide +
                ", index2letterLeftSide=" + Arrays.toString(index2letterLeftSide) +
                ", letterSize=" + letterSize +

                ", currentMapSize=" + currentMapSize +
                ", windowPosition=" + windowPosition +
                ", notchPosition=" + notchPosition +
                ", rotorID=" + rotorID +
                ", initialWindowPosition=" + initialWindowPosition +
                '}';
    }
}
