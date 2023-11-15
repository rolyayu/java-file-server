package net.lacit.file_server.server;

import com.sun.net.httpserver.HttpServer;
import net.lacit.file_server.server.interfaces.Endpoint;

import java.io.IOException;
import java.net.InetSocketAddress;

public class Server {
    private final HttpServer httpServer;

    public Server(int port) throws IOException {
        httpServer = HttpServer.create(new InetSocketAddress("localhost", port), 0);
    }

    public void addEndpoint(Endpoint endpoint) {
        httpServer.createContext(endpoint.getEndpoint(), endpoint.getHandler());
    }

    public void start() {
        httpServer.start();
    }
}
