package users;

import allyDTOs.AllyDataDTO;

import java.io.InputStream;
import java.util.*;

public class UBoatManager {


    private final Map<String, Set<AllyDataDTO>> uboatMapAlliesSet;
    private final Map<String, InputStream> uboatMapXmlFilesSet;

    public UBoatManager() {
        this.uboatMapAlliesSet = new HashMap<>();
        uboatMapXmlFilesSet=new HashMap<>();
    }


    public void assignAllyToUboat(AllyDataDTO agentDataDTO,String uboatName)
    {

        if (uboatMapAlliesSet.containsKey(uboatName)) {
            uboatMapAlliesSet.get(uboatName).add(agentDataDTO);
        }


    }
    public void addUboatUser(String uboatName)
    {
        if(!uboatMapAlliesSet.containsKey(uboatName)) {
            uboatMapAlliesSet.put(uboatName, new HashSet<>());
            uboatMapXmlFilesSet.put(uboatName, null);
        }
        else
        {
            System.out.println("error");
            //TODO: Runtime Exception
        }

    }
    public void assignXMLFileToUboat(String uboatName,InputStream inputStream)
    {
        if (uboatMapAlliesSet.containsKey(uboatName))
              uboatMapXmlFilesSet.put(uboatName,inputStream);
        else
            {
                System.out.println("error");
                //TODO: Runtime Exception
            }

    }
    public boolean isUboatExist(String uboatName)
    {
        return uboatMapAlliesSet.containsKey(uboatName);
    }
    public Set<AllyDataDTO> getAlliesDataForUboat(String uboatName)
    {
        return Collections.unmodifiableSet(uboatMapAlliesSet.get(uboatName));
    }
}
