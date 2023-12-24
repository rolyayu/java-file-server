package net.lacit.file_server.server.exceptions;

public class EndpointException extends RuntimeException {
    private final short code;

    private final String response;

    public EndpointException(short code, String response) {
        this.code = code;
        this.response = response;
    }

    public short getCode() {
        return code;
    }

    public String getResponse() {
        return response;
    }
}
