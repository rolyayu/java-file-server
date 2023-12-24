package net.lacit.file_server.server.exceptions;

public class BadRequestException extends EndpointException {
    public BadRequestException(String response) {
        super((short) 400, response);
    }
}
