package enigmaEngine;

public interface Encryptor {

    String processDataInput(String dataInput);
    char processDataInput(char charInput);
    void resetProcessingTime();

}
