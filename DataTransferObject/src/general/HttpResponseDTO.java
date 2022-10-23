package general;

public class HttpResponseDTO {

    private final int code;
    private final String body;

    public HttpResponseDTO(int code, String body) {
        this.code = code;
        this.body = body;
    }

    public int getCode() {
        return code;
    }

    public String getBody() {
        return body;
    }
}
