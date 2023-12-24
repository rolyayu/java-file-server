package net.lacit.file_server.utils;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.File;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class FilePathUtilsTest {

    private FilePathUtils filePathUtils;

    @Mock
    private PropertyReader mockReader;

    @BeforeEach
    void beforeEach() {
        filePathUtils = new FilePathUtils(mockReader);
    }

    @Test
    void FilePathUtils_BuildPath_ReturnBuiltFilePath() {
        given(mockReader.readProperty(anyString())).willReturn("repo");
        String url = "/file";

        String builtPath = filePathUtils.buildFilePath(url).toString();

        Assertions.assertEquals("repo" + File.separator + "file", builtPath);
        verify(mockReader, times(1)).readProperty(anyString());
    }

    @Test
    void FilePathUtils_ExtractFileName_ReturnFileWithExtension() {
        String url = "test" + File.separator + "file.ext";

        String extractedFilename = filePathUtils.extractFileName(url);

        Assertions.assertEquals("file.ext", extractedFilename);
    }

    @Test
    void FilePathUtils_ExtractFolders_ReturnFoldersJoinedWithSeparator() {
        String url = "test" + File.separator + "test1" + File.separator + "file.ext";

        String extractedFolders = filePathUtils.extractFolders(url);

        Assertions.assertEquals("test" + File.separator + "test1",extractedFolders);
    }
}
