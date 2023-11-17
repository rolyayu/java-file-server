package net.lacit.file_server.server.upload;

import com.sun.net.httpserver.HttpExchange;
import net.lacit.file_server.server.exceptions.NotFoundException;
import net.lacit.file_server.server.upload.downloaders.FileDownloader;
import net.lacit.file_server.server.upload.loaders.FileLoader;
import net.lacit.file_server.utils.FilePathUtils;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;

public class UploadServiceImpl implements UploadService {
    private final FilePathUtils filePathUtils;

    private final FileDownloader fileDownloader;

    private final FileLoader fileLoader;

    public UploadServiceImpl(FilePathUtils filePathUtils, FileDownloader fileDownloader, FileLoader fileLoader) {
        this.filePathUtils = filePathUtils;
        this.fileDownloader = fileDownloader;
        this.fileLoader = fileLoader;
    }

    @Override
    public void loadFile(HttpExchange exchange) throws IOException {
        Path filePath = filePathUtils.buildFilePath(exchange.getRequestURI().toString());
        String fileName = filePathUtils.extractFileName(filePath.toString());
        if (!Files.exists(filePath)) {
            throw new NotFoundException(String.format("File %s not found", filePath));
        }
        try {
            fileLoader.load(fileName, exchange.getResponseBody());
            exchange.getResponseHeaders().set("Content-Disposition", String.format("attachment; filename=\"%s\"", fileName));
            exchange.sendResponseHeaders(200, 0);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void downloadFile(HttpExchange exchange) throws IOException {
        Path filePath = filePathUtils.buildFilePath(exchange.getRequestURI().toString());
        String folders = filePathUtils.extractFolders(filePath.toString());

        Files.createDirectories(Path.of(folders));
        if (!Files.exists(filePath)) {
            Files.createFile(filePath);
        }

        try {
            fileDownloader.download(filePath.toString(), exchange.getRequestBody());
            String response = String.format("File %s has been downloaded successfully", filePath);
            exchange.sendResponseHeaders(200, response.getBytes().length);
            OutputStream os = exchange.getResponseBody();
            os.write(response.getBytes());
            os.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
