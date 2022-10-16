package UBoatDTO;

public enum GameStatus{
    ACTIVE("active"),
    IDLE("idle"),
    FINISH("finish");


    final String status;
    GameStatus(String level) {
        status=level;
    }
    @Override
    public String toString() {
        return status;
    }
}