package users;

import general.ApplicationType;
import general.UserListDTO;

import java.util.*;


/*
Adding and retrieving users is synchronized and in that manner - these actions are thread safe
Note that asking if a user exists (isUserExists) does not participate in the synchronization and it is the responsibility
of the user of this class to handle the synchronization of isUserExists with other methods here on it's own
 */
public class UserManager{

    private final Set<String> uboatSet;
    private final Set<String> alliesSet;
    private final Set<String> agentSet;
    public UserManager() {
        uboatSet = new HashSet<>();
        alliesSet = new HashSet<>();
        agentSet = new HashSet<>();

    }
    public synchronized void addUserName(String username, ApplicationType type)
    {
        switch (type)
        {
            case UBOAT:
                uboatSet.add(username);

                break;
            case ALLY:
                alliesSet.add(username);
                break;
            case AGENT:
                agentSet.add(username);
                break;
        }

    }

    public boolean isUserAgentExist(String agentName)
    {
        if(agentSet.contains(agentName))
            return true;
        else return false;
    }
    public synchronized void removeUserName(String username, ApplicationType type)
    {
        switch (type)
        {
            case UBOAT:
                uboatSet.remove(username);
                break;
            case ALLY:
                alliesSet.remove(username);
                break;
            case AGENT:
                agentSet.remove(username);
                break;
        }

    }
    public void clearAll()
    {
        uboatSet.clear();
        agentSet.clear();
        alliesSet.clear();
    }

    public synchronized UserListDTO getUsers() {

      return new UserListDTO(Collections.unmodifiableSet(uboatSet),
                              Collections.unmodifiableSet(alliesSet),
                              Collections.unmodifiableSet(agentSet));
    }

    public boolean isUserExists(String username) {
        return uboatSet.contains(username) || alliesSet.contains(username) || agentSet.contains(username);
    }
}
