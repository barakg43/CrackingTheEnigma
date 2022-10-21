package TEST_GUI;

import Ally.AllyDataManager;
import allyDTOs.AllyDataDTO;

import java.util.HashSet;
import java.util.Set;

public class Tester {

    public static void main(String[] args) {


        AllyDataManager allyDataManager=new AllyDataManager("ally1");

        Set<AllyDataDTO> allyDataDTOS=new HashSet<>();
        allyDataDTOS.add(allyDataManager);


        for( AllyDataDTO allyDataDTO:allyDataDTOS)
        {
            allyDataManager.increaseAgentNumber();
            allyDataManager.setTaskSize(500);
            System.out.println("inset"+allyDataDTO);
        }
    }
}
