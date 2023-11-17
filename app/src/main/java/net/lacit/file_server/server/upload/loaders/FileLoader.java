package net.lacit.file_server.server.upload.loaders;

import java.io.OutputStream;

public interface FileLoader {
    void load(String url, OutputStream dest) throws Exception;
}
