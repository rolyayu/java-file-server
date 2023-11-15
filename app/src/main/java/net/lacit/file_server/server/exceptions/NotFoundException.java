package net.lacit.file_server.server.exceptions;

public class NotFoundException extends EndpointException {
    public NotFoundException(String response) {
        super((short) 404, response);
    }
}
