package impl;



import jaxb.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Rotor {

    //right side of the rotor
    private Map<Character,Integer> latter2IndexRightSide=null;
    private char[] index2latterRightSide=null;
    //left side of the rotor
    private Map<Character,Integer> latter2IndexLeftSide=null;
    private char[] index2latterLeftSide=null;
    private final int latterSize;
    private int currentMapSize=0;
    private int windowPosition=0;
    private final int notchPosition;
    private final int rotorID;
    private int initialWindowPosition;
    private final boolean debugMode;
       public Rotor(int latterSize, int notch, int id, boolean debugMode) {
        this.latterSize = latterSize;
        this.notchPosition = notch-1;
        this.rotorID = id;
        this.debugMode = debugMode;
        initRotorArrays();
    }
    public Rotor(int latterSize, int notch, int id) {
        this(latterSize, notch, id, false);
    }
    public int getNotchLocation() {
        return notchPosition;
    }

    public int getRotorID() {
        return rotorID;
    }

    public void setRightSide(List<CTEPositioning> positioningList)
    {
        for(int i=0;i<positioningList.size();i++)
        {
            char letter=positioningList.get(i).getRight().charAt(0);
            if(latter2IndexRightSide.containsKey(letter))
                throw new RuntimeException("In rotor No '"+rotorID+ "' the letter " + letter + " is already mapped. Please check that each character mapped only once.");
            latter2IndexRightSide.put(letter,i);
            index2latterRightSide[i]=letter;
        }
    }

    public void setLeftSide(List<CTEPositioning> positioningList)
    {
        for(int i=0;i<positioningList.size();i++)
        {
            char letter=positioningList.get(i).getLeft().charAt(0);

            if(latter2IndexLeftSide.containsKey(letter))
                throw new RuntimeException("In rotor No '"+rotorID+ "' the letter " +letter + " is already mapped. Please check that each character mapped only once.");
            latter2IndexLeftSide.put(letter,i);
            index2latterLeftSide[i]=letter;
        }
    }

 public void setInitialWindowPosition(char latter) {
        initialWindowPosition = latter2IndexRightSide.get(latter);
        resetWindowPositionToInitialPosition();

    }

private void initRotorArrays() {
        index2latterRightSide = new char[latterSize];
        index2latterLeftSide = new char[latterSize];
        latter2IndexLeftSide = new HashMap<>(latterSize);
        latter2IndexRightSide = new HashMap<>(latterSize);
        for (int i = 0; i < latterSize; i++) {
            index2latterLeftSide[i] = index2latterRightSide[i] = 0;
        }
    }
    public void resetWindowPositionToInitialPosition() {
        windowPosition = initialWindowPosition;
    }

    public void addMapLatterToRotor(char leftLatter, char rightLatter) {
        if (currentMapSize > latterSize)
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


    int calcIndexRotorTable(int index, boolean isRelativIndex) {
        int value;
        if (isRelativIndex)
            value = index - windowPosition;
        else
            value = index + windowPosition;
        //(a % b + b) % b get the correct answer also for (negative number)%(positive number)
        return (value % latterSize + latterSize) % latterSize;

    }

    private void forwardWindowPosition(boolean previousNotchRotorArriveWindow) {
        if (previousNotchRotorArriveWindow) {
            windowPosition++;
            windowPosition = windowPosition % latterSize;
        }
        if (debugMode)
            System.out.format("Rotor ID:%d,window position:%d,notch position:%d\n", this.rotorID, windowPosition, notchPosition);
    }
    /**
     * map input line to output line on rotor table
     * @param inputRowRotorTable the input line number of the rotor table
     * @param isInputFromLeft the input to rotor is from right(false) or from left(true)
     * @param previousNotchRotorArriveWindow true make the rotor advance the window position
     * @return RotorOutputData object that contain data after
     */
    public RotorOutputData getOutputMapIndex(int inputRowRotorTable, boolean isInputFromLeft, boolean previousNotchRotorArriveWindow) {
        char inputLatter;
        forwardWindowPosition(previousNotchRotorArriveWindow);
        boolean advanceNextRotor = (notchPosition == windowPosition);
        int relativeIndex = calcIndexRotorTable(inputRowRotorTable, false);
        int otherSideRelativIndex;
        if (debugMode)
            System.out.format("Rotor ID:%d,latter:%c\n", this.rotorID, index2latterRightSide[relativeIndex]);
        if (isInputFromLeft) {
            inputLatter = index2latterLeftSide[relativeIndex];//translate the input index to latter in left side
            otherSideRelativIndex = calcIndexRotorTable(latter2IndexRightSide.get(inputLatter), true);
        } else {
            inputLatter = index2latterRightSide[relativeIndex];//translate the input index to latter in right side
            otherSideRelativIndex = calcIndexRotorTable(latter2IndexLeftSide.get(inputLatter), true);
        }
        if (debugMode)
            System.out.format("Rotor ID:%d,input latter:%c,input flow:%d -> %d\n", this.rotorID, inputLatter, inputRowRotorTable, otherSideRelativIndex);
        return new RotorOutputData(otherSideRelativIndex, advanceNextRotor);
    }

    public boolean checkIfAllLatterMapped() {
        return latter2IndexLeftSide.size() == latterSize && latter2IndexRightSide.size() == latterSize;
    }

    public void printRotorTableDebugModeOnly()
    {
        if(debugMode)
        {
            System.out.println("Rotor id:"+rotorID);
            for (int i = 0; i < latterSize; i++) {
                System.out.println(index2latterLeftSide[i]+" "+index2latterRightSide[i]);

            }
        }
    }

}
