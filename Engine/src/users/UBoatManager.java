package users;

import allyDTOs.AllyDataDTO;

import java.io.InputStream;
import java.util.*;

public class UBoatManager {


    private final Map<String, Set<AllyDataDTO>> uboatMapAlliesSet;
    private final Map<String, String> uboatMapXmlFilesSet;

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
        }
        else
        {
            System.out.println("error");
            //TODO: Runtime Exception
        }

    }
    public void assignXMLFileToUboat(String uboatName,String XmlContent)
    {
        if (uboatMapAlliesSet.containsKey(uboatName))
              uboatMapXmlFilesSet.put(uboatName,XmlContent);
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
