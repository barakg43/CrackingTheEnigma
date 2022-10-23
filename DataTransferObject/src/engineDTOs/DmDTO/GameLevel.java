package engineDTOs.DmDTO;

public enum GameLevel {
    EASY("Easy"),
    MEDIUM("Medium"),
    HARD("Hard"),
    INSANE("Insane");
    final String name;
    GameLevel(String level) {
        name=level;
    }
    @Override
    public String toString() {
        return name;
    }
}
