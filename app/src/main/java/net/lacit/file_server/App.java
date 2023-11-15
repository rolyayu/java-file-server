package net.lacit.file_server;

import net.lacit.file_server.server.Server;
import net.lacit.file_server.server.interfaces.Endpoint;
import net.lacit.file_server.server.upload.UploadEndpoint;
import net.lacit.file_server.server.upload.UploadService;
import net.lacit.file_server.server.upload.UploadServiceImpl;
import net.lacit.file_server.server.upload.UrlValidator;
import net.lacit.file_server.utils.FilePathUtils;
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
        PropertyReader reader = new PropertyReader();
        int port = Integer.parseInt(reader.readProperty("port").toString());

        Server server = new Server(port);
        UrlValidator urlValidator = new UrlValidator();
        FilePathUtils filePathUtils = new FilePathUtils(reader);
        UploadService uploadService = new UploadServiceImpl(filePathUtils);
        Endpoint uploadEndpoint = new UploadEndpoint(uploadService, urlValidator);

        server.addEndpoint(uploadEndpoint);
        server.start();
    }
}
