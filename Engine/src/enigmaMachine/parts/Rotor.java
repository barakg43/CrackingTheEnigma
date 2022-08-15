package enigmaMachine.parts;


import java.io.Serializable;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;


public class Rotor implements Serializable {

    //right side of the rotor
    private Map<Character,Integer> latter2IndexRightSide=null;
    private char[] index2latterRightSide=null;
    //left side of the rotor
    private Map<Character,Integer> latter2IndexLeftSide=null;
    private char[] index2latterLeftSide=null;
    private final int letterSize;
    private int currentMapSize=0;
    private int windowPosition=0;
    private final int notchPosition;
    private final int rotorID;
    private int initialWindowPosition;
    private final boolean debugMode;
       public Rotor(int letterSize, int notch, int id, boolean debugMode) {
        this.letterSize = letterSize;
        this.notchPosition = notch-1;
        this.rotorID = id;
        this.debugMode = debugMode;
        initRotorArrays();
    }
    public Rotor(int letterSize, int notch, int id) {
        this(letterSize, notch, id, false);
    }

    public int getRotorID() {
        return rotorID;
    }

    public int getNotchPosition()
    {
        return notchPosition;
    }

    public void setInitialWindowPosition(char latter) {
        initialWindowPosition = latter2IndexRightSide.get(latter);
        resetWindowPositionToInitialPosition();

    }

private void initRotorArrays() {
        index2latterRightSide = new char[letterSize];
        index2latterLeftSide = new char[letterSize];
        latter2IndexLeftSide = new HashMap<>(letterSize);
        latter2IndexRightSide = new HashMap<>(letterSize);
        for (int i = 0; i < letterSize; i++) {
            index2latterLeftSide[i] = index2latterRightSide[i] = 0;
        }
    }
    public void resetWindowPositionToInitialPosition() {
        windowPosition = initialWindowPosition;
    }

    public void addMapLatterToRotor(char leftLatter, char rightLatter) {
        if (currentMapSize > letterSize)
            throw new RuntimeException("overflow latter size,too many mapped latter!");
        if (latter2IndexLeftSide.containsKey(leftLatter))
            throw new RuntimeException(String.format("the latter %c is already in roter table with %c", leftLatter, index2latterRightSide[latter2IndexLeftSide.get(leftLatter)]));
        if (latter2IndexRightSide.containsKey(rightLatter))
            throw new RuntimeException(String.format("the latter %c is already in roter table with %c", rightLatter, index2latterLeftSide[latter2IndexRightSide.get(rightLatter)]));
        index2latterRightSide[currentMapSize] = rightLatter;
        index2latterLeftSide[currentMapSize] = leftLatter;

        latter2IndexLeftSide.put(leftLatter, currentMapSize);
        latter2IndexRightSide.put(rightLatter, currentMapSize);
        currentMapSize++;
    }


   public int calcIndexRotorTable(int index, boolean isInnerTableIndex) {

        int value;
        if (isInnerTableIndex)
            value = index - windowPosition;//the index is real inner index table
        else
            value = index + windowPosition;//outer index that came from outer source
        //(a % b + b) % b get the correct answer also for (negative number)%(positive number)
        return (value % letterSize + letterSize) % letterSize;

    }
    public int calcDistanceFromNotchToWindowPosition()
    {
        return calcIndexRotorTable(notchPosition,true);

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
        if (debugMode)
            System.out.format("Rotor ID:%d,window position:%d,notch position:%d\n", this.rotorID, windowPosition, notchPosition);

        return  notchPosition == windowPosition;
    }
    /**
     * map input line to output line on rotor table
     * @param inputRowRotorTable the input line number of the rotor table
     * @param isInputFromLeft the input to rotor is from right(false) or from left(true)
     * @return output row from rotor table that determined by input letter in table
     */
    public Integer getOutputMapIndex(int inputRowRotorTable, boolean isInputFromLeft) {
        char inputLetter;
        int relativeIndex = calcIndexRotorTable(inputRowRotorTable, false);
        int otherSideInnerIndex;
        if (debugMode)
            System.out.format("Rotor ID:%d,latter:%c\n", this.rotorID, index2latterRightSide[relativeIndex]);
        if (isInputFromLeft) {
            inputLetter = index2latterLeftSide[relativeIndex];//translate the input index to latter in left side
            otherSideInnerIndex = calcIndexRotorTable(latter2IndexRightSide.get(inputLetter), true);
        } else {
            inputLetter = index2latterRightSide[relativeIndex];//translate the input index to latter in right side
            otherSideInnerIndex = calcIndexRotorTable(latter2IndexLeftSide.get(inputLetter), true);
        }
        if (debugMode)
            System.out.format("Rotor ID:%d,input latter:%c,input flow:%d -> %d\n", this.rotorID, inputLetter, inputRowRotorTable, otherSideInnerIndex);
        return otherSideInnerIndex;
    }

    public boolean checkIfAllCharacterMapped() {
        return latter2IndexLeftSide.size() == letterSize && latter2IndexRightSide.size() == letterSize;
    }

    public void printRotorTableDebugModeOnly()
    {
        if(debugMode)
        {
            System.out.println("Rotor id:"+rotorID);
            for (int i = 0; i < letterSize; i++) {
                System.out.println(index2latterLeftSide[i]+" "+index2latterRightSide[i]);

            }
        }
    }

    @Override
    public String toString() {
        return "Rotor{" +
                "latter2IndexRightSide=" + latter2IndexRightSide +
                ", index2latterRightSide=" + Arrays.toString(index2latterRightSide) +
                ", latter2IndexLeftSide=" + latter2IndexLeftSide +
                ", index2latterLeftSide=" + Arrays.toString(index2latterLeftSide) +
                ", latterSize=" + letterSize +
                ", currentMapSize=" + currentMapSize +
                ", windowPosition=" + windowPosition +
                ", notchPosition=" + notchPosition +
                ", rotorID=" + rotorID +
                ", initialWindowPosition=" + initialWindowPosition +
                '}';
    }
}
