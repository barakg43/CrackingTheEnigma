package engineDTOs.DmDTO;

public enum BruteForceLevel {
    EASY("Easy"),
    MIDDLE("Middle"),
    HARD("Hard"),
    INSANE("Insane");
    final String name;
    BruteForceLevel(String level) {
        name=level;
    }
    @Override
    public String toString() {
        return name;
    }
}
