package net.lacit.file_server.server.upload;

import com.sun.net.httpserver.HttpHandler;
import net.lacit.file_server.server.exceptions.EndpointException;
import net.lacit.file_server.server.interfaces.Endpoint;

public class UploadEndpoint implements Endpoint {

    private final UploadService uploadService;

    private final UrlValidator urlValidator;


    public UploadEndpoint(UploadService uploadService, UrlValidator urlValidator) {
        this.uploadService = uploadService;
        this.urlValidator = urlValidator;
    }

    @Override
    public String getEndpoint() {
        return "/";
    }

    @Override
    public HttpHandler getHandler() {
        return (exchange -> {
            try {
                urlValidator.validateUrl(exchange);
                if (exchange.getRequestMethod().equals("GET")) {
                    uploadService.loadFile(exchange);
                } else if (exchange.getRequestMethod().equals("POST")) {
                    uploadService.downloadFile(exchange);
                } else {
                    String response = "Unsupported method";
                    exchange.sendResponseHeaders(405, response.getBytes().length);
                    var os = exchange.getResponseBody();
                    os.write(response.getBytes());
                    os.close();
                }
            } catch (EndpointException e) {
                exchange.sendResponseHeaders(e.getCode(), e.getResponse().getBytes().length);
                var os = exchange.getResponseBody();
                os.write(e.getResponse().getBytes());
                os.close();
            } catch (Exception e) {
                String response = "Something bad happened: " + e.getMessage();
                exchange.sendResponseHeaders(500, response.getBytes().length);
                var os = exchange.getResponseBody();
                os.write(response.getBytes());
                os.close();
            }
        });
    }
}
