package net.lacit.file_server.server.upload.loaders;

import java.io.*;
import java.nio.file.Path;

public class LocalFileLoader implements FileLoader{
    @Override
    public void load(String url, OutputStream dest) throws Exception{
        Path filePath = Path.of(url);
        try (InputStream stream = new FileInputStream(filePath.toFile())) {
            stream.transferTo(dest);
        }
    }
}
