package net.lacit.file_server.utils;

import net.lacit.file_server.utils.PropertyReader;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.UUID;

public class PropertyReaderTest {
    private final PropertyReader propertyReader;

    {
        propertyReader = new PropertyReader();
    }

    @Test
    void PropertyReader_ReadProperty_ReturnFoundedProperty() {
        String keyPort = "port";
        String keyRepo = "files_folder";

        Object foundedPort = propertyReader.readProperty(keyPort);
        Object foundedRepo = propertyReader.readProperty(keyRepo);

        Assertions.assertEquals(3000,Integer.parseInt(foundedPort.toString()));
        Assertions.assertEquals("repo", foundedRepo.toString());
    }

    @Test
    void PropertyReader_ReadProperty_ThrowsWhenPropertyNotFound() {
        String randomKey = UUID.randomUUID().toString();

        Assertions.assertThrows(RuntimeException.class, () -> {
           propertyReader.readProperty(randomKey);
        });
    }

    @Test
    void PropertyReader_FillProperties_ThrowsWhenFileNotFound() {
        String randomFileName = UUID.randomUUID().toString();

        Assertions.assertThrows(RuntimeException.class, () -> {
           new PropertyReader(randomFileName);
        });
    }
}
