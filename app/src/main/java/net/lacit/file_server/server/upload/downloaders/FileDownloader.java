package net.lacit.file_server.server.upload.downloaders;

import java.io.InputStream;

public interface FileDownloader {
    void download(String url, InputStream source) throws Exception;
}
