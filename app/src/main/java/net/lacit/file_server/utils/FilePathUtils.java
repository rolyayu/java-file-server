package net.lacit.file_server.utils;

import java.io.File;
import java.nio.file.Path;

public class FilePathUtils {
    private final PropertyReader propertyReader;

    public FilePathUtils(PropertyReader propertyReader) {
        this.propertyReader = propertyReader;
    }

    public Path buildFilePath(String requestURL) {
        return Path.of(
                String.valueOf(propertyReader.readProperty("files_folder"))
                        + File.separatorChar
                        + requestURL.substring(1).replace('/', File.separatorChar)
        );
    }

    public String extractFileName(String filePath) {
        var parts = filePath.split(String.format("\\%s", File.separator));
        return parts[parts.length - 1];
    }

    public String extractFolders(String filePath) {
        return filePath.substring(0,filePath.lastIndexOf(File.separatorChar));
    }
}
