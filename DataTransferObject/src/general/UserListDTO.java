package general;

import java.util.Set;

public class UserListDTO {


   private final Set<String> uboatUsersSet;
    private final Set<String> alliesUsersSet;
    private final Set<String> agentsUsersSet;

    public UserListDTO(Set<String> uboatUsersSet, Set<String> alliesUsersSet, Set<String> agentsUsersSet) {
        this.uboatUsersSet = uboatUsersSet;
        this.alliesUsersSet = alliesUsersSet;
        this.agentsUsersSet = agentsUsersSet;
    }

    public Set<String> getUboatUsersSet() {
        return uboatUsersSet;
    }

    public Set<String> getAlliesUsersSet() {
        return alliesUsersSet;
    }

    public Set<String> getAgentsUsersSet() {
        return agentsUsersSet;
    }
}
