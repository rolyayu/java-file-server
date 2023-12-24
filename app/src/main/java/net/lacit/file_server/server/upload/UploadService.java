package net.lacit.file_server.server.upload;

import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;

public interface UploadService {
    void loadFile(HttpExchange exchange) throws IOException;
    void downloadFile(HttpExchange exchange) throws IOException;
}
