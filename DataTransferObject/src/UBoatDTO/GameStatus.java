package UBoatDTO;

public enum GameStatus{
    ACTIVE("active"),
    IDLE("idle"),
    WAITING_FOR_ALLIES("waiting all allies "),
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