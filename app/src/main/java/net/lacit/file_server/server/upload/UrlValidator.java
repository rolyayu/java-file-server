package net.lacit.file_server.server.upload;

import com.sun.net.httpserver.HttpExchange;
import net.lacit.file_server.server.exceptions.BadRequestException;
import net.lacit.file_server.server.exceptions.UnsupportedMediaTypeException;

import java.util.HashMap;
import java.util.Map;

public class UrlValidator {
    private final Map<String, String> CONTENT_TYPES_WITH_EXTENSIONS;

    public UrlValidator() {
        CONTENT_TYPES_WITH_EXTENSIONS = new HashMap<>();
        CONTENT_TYPES_WITH_EXTENSIONS.put("application/xml", ".xml");
        CONTENT_TYPES_WITH_EXTENSIONS.put("application/pdf", ".pdf");
        CONTENT_TYPES_WITH_EXTENSIONS.put("text/plain", ".txt");
        CONTENT_TYPES_WITH_EXTENSIONS.put("text/html", ".html");
        CONTENT_TYPES_WITH_EXTENSIONS.put("image/png", ".png");
    }

    public void validateUrl(HttpExchange exchange) {
        String pathUrl = exchange.getRequestURI().toString();
        if (pathUrl.length() <= 1) {
            throw new BadRequestException("No file given");
        }
        String fileExtension = pathUrl.substring(pathUrl.lastIndexOf('.'));
        if (!CONTENT_TYPES_WITH_EXTENSIONS.containsValue(fileExtension)) {
            throw new UnsupportedMediaTypeException("Unsupported file extension");
        }
        if (exchange.getRequestMethod().equals("POST")) {
            validatePost(exchange, fileExtension);
        }
        int dotsCount = pathUrl.split("\\.").length - 1;
        if (dotsCount != 1) {
            throw new BadRequestException("Unsupported amount of files");
        }
    }

    private void validatePost(HttpExchange exchange, String fileExtension) {

        var contentTypes = exchange.getRequestHeaders().get("Content-Type");
        if (contentTypes.stream().anyMatch(type -> type.startsWith("multipart/form-data"))) {
            return;
        }
        if (contentTypes.stream().noneMatch(CONTENT_TYPES_WITH_EXTENSIONS::containsKey)) {
            throw new UnsupportedMediaTypeException("Unsupported Content-Type");
        }
        if (contentTypes.stream().noneMatch(type -> CONTENT_TYPES_WITH_EXTENSIONS.get(type).equals(fileExtension))) {
            throw new BadRequestException(String.format("File type %s doesn't match with given Content-Types",
                    fileExtension));
        }
    }
}
