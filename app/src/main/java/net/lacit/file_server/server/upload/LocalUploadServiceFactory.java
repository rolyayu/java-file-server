package net.lacit.file_server.server.upload;

import net.lacit.file_server.server.upload.downloaders.FileDownloader;
import net.lacit.file_server.server.upload.downloaders.LocalFileDownloader;
import net.lacit.file_server.server.upload.loaders.FileLoader;
import net.lacit.file_server.server.upload.loaders.LocalFileLoader;
import net.lacit.file_server.utils.FilePathUtils;
import net.lacit.file_server.utils.PropertyReader;

public class LocalUploadServiceFactory implements UploadServiceFactory{
    @Override
    public UploadService getService() {
        return new LocalUploadService(
                new FilePathUtils(PropertyReader.getInstance()),
                this.getDownloader(),
                this.getLoader()
        );
    }

    @Override
    public FileDownloader getDownloader() {
        return new LocalFileDownloader();
    }

    @Override
    public FileLoader getLoader() {
        return new LocalFileLoader();
    }
}
