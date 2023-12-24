package net.lacit.file_server.server.upload;

import net.lacit.file_server.server.upload.downloaders.FileDownloader;
import net.lacit.file_server.server.upload.loaders.FileLoader;

public interface UploadServiceFactory {
    UploadService getService();

    FileDownloader getDownloader();

    FileLoader getLoader();
}
