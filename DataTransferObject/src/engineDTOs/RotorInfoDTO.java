package engineDTOs;

import java.io.Serializable;

public class RotorInfoDTO implements Serializable {
    private final int id;
    private final int distanceToWindow;
    private final char statingLetter;

//    public static class RotorInfoPropertyDTO{
//        SimpleStringProperty id;
//        SimpleStringProperty distanceToWindow;
//        SimpleStringProperty statingLetter;
//
//        public SimpleStringProperty idProperty() {
//            return id;
//        }
//
//        public SimpleStringProperty distanceToWindowProperty() {
//            return distanceToWindow;
//        }
//
//        public SimpleStringProperty statingLetterProperty() {
//            return statingLetter;
//        }
//
//        public RotorInfoPropertyDTO(SimpleStringProperty id, SimpleStringProperty distanceToWindow, SimpleStringProperty statingLetter){
//            this.id=id;
//            this.distanceToWindow=distanceToWindow;
//            this.statingLetter=statingLetter;
//        }
//
//        public String getId() {
//            return id.get();
//        }
//        public String getDistanceToWindow() {
//            return distanceToWindow.get();
//        }
//        public String getStatingLetter() {
//            return statingLetter.get();
//        }
//
//    }

    public RotorInfoDTO(int id,int distanceToWindow,char statingLetter){
        this.id=id;
        this.distanceToWindow=distanceToWindow;
        this.statingLetter=statingLetter;

    }

    public int getId() {
        return id;
    }

    public int getDistanceToWindow() {
        return distanceToWindow;
    }

    public char getStatingLetter() {
        return statingLetter;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 17 * hash +id+distanceToWindow+statingLetter;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final RotorInfoDTO other = (RotorInfoDTO) obj;
        return (id == other.id) && (distanceToWindow == other.distanceToWindow) && (statingLetter == other.statingLetter);
    }
}
