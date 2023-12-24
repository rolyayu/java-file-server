package net.lacit.file_server.server.exceptions;

public class UnsupportedMediaTypeException extends EndpointException {
    public UnsupportedMediaTypeException(String response) {
        super((short) 415, response);
    }
}
