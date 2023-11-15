package net.lacit.file_server.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertyReader {

    private final String propertiesFile;

    private Properties properties;

    public PropertyReader(String propertiesFile) {
        this.propertiesFile = propertiesFile;
        fillProperties();
    }

    public PropertyReader() {
        this.propertiesFile = "application.properties";
        fillProperties();
    }

    private void fillProperties() {
        try (InputStream input = PropertyReader.class.getClassLoader().getResourceAsStream(propertiesFile)) {
            properties = new Properties();
            properties.load(input);
        } catch (IOException e) {
            throw new RuntimeException(String.format("Error while reading %s: %s", propertiesFile, e.getMessage()));
        }
    }

    public Object readProperty(String key) throws RuntimeException {
        Object value = properties.get(key);
        if (value == null) {
            throw new RuntimeException(String.format("Key %s in not" + " found in %s file", key, propertiesFile));
        }
        return value;
    }
}
