package net.lacit.file_server.server.interfaces;

import com.sun.net.httpserver.HttpHandler;

public interface Endpoint {
    String getEndpoint();
    HttpHandler getHandler();
}
