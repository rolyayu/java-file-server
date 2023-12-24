package net.lacit.file_server.server.upload.downloaders;

import net.lacit.file_server.server.exceptions.UnsupportedMediaTypeException;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.Set;

public class LocalFileDownloader implements FileDownloader{
    private final Set<String> PLAIN_TYPES;

    private final Set<String> MULTYPART_TYPES;

    public LocalFileDownloader() {
        PLAIN_TYPES = new HashSet<>();
        MULTYPART_TYPES = new HashSet<>();

        PLAIN_TYPES.add(".txt");
        PLAIN_TYPES.add(".html");
        PLAIN_TYPES.add(".xml");

        MULTYPART_TYPES.add(".pdf");
        MULTYPART_TYPES.add(".png");
    }

    @Override
    public void download(String url, InputStream content) throws Exception {
        String extension = url.substring(url.lastIndexOf('.'));
        Path savePath = Path.of(url);
        if (PLAIN_TYPES.contains(extension)) {
            this.downloadPlain(savePath, content);
        } else if (MULTYPART_TYPES.contains(extension)) {
            this.downloadComplex(savePath, content);
        } else {
            throw new UnsupportedMediaTypeException(String.format("Cannot save file with %s extension", extension));
        }
    }

    private void downloadPlain(Path savePath, InputStream content) throws IOException {
        Files.write(savePath, content.readAllBytes());
    }

    private void downloadComplex(Path savePath, InputStream content) throws IOException {
        skipHeaders(content);
        int breakLineCounter = 0;
        File tempFile = File.createTempFile("pref-","-suf");
        try (OutputStream outputStream = new FileOutputStream(tempFile)) {
            int currentByte;
            while ((currentByte=content.read())!=-1) {
                if((char) currentByte=='\n') {
                    breakLineCounter++;
                }
                outputStream.write(currentByte);
            }
        }
        try (OutputStream outputStream = new FileOutputStream(savePath.toFile())) {
            try (InputStream tempInputStream = new FileInputStream(tempFile)) {
                int currentByte;
                int currentBreakLineNum = 0;
                while (currentBreakLineNum<breakLineCounter-1) {
                    currentByte = tempInputStream.read();
                    if((char) currentByte=='\n') {
                        currentBreakLineNum++;
                    }
                    outputStream.write(currentByte);
                }
            }
            tempFile.deleteOnExit();
        }
    }

    private void skipHeaders(InputStream inputStream) throws IOException {
        int currentByte;
        int breakLineCounter = 0;
        while (breakLineCounter != 4) {
            currentByte = inputStream.read();
            if (currentByte == -1) {
                return;
            }
            if ((char) currentByte == '\n') {
                breakLineCounter++;
            }
        }
    }
}
