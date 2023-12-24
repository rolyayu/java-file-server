package net.lacit.file_server;

import net.lacit.file_server.server.Server;
import net.lacit.file_server.server.interfaces.Endpoint;
import net.lacit.file_server.server.upload.*;
import net.lacit.file_server.utils.PropertyReader;

import java.io.IOException;

public class App {

    public static void main(String[] args) {
        try {
            bootstrapServer();
        } catch (IOException e) {
            System.out.println("Cannot bootstrap server");
        }
    }

    private static void bootstrapServer() throws IOException {
        PropertyReader reader = PropertyReader.getInstance();
        int port = Integer.parseInt(reader.readProperty("port").toString());

        Server server = new Server(port);
        UrlValidator urlValidator = new UrlValidator();
        UploadServiceFactory uploadServiceFactory = new LocalUploadServiceFactory();
        UploadService localUploadService = uploadServiceFactory.getService();
        Endpoint uploadEndpoint = new UploadEndpoint(localUploadService, urlValidator);

        server.addEndpoint(uploadEndpoint);
        server.start();
    }
}
