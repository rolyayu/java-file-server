package net.lacit.file_server.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertyReader {

    private final Properties properties;

    private final String PROPERTIES_FILE = "application.properties";

    private static class PropertyReaderHolder {
        private static final PropertyReader INSTANCE = new PropertyReader();
    }

    public static PropertyReader getInstance() {
        return PropertyReaderHolder.INSTANCE;
    }

    private PropertyReader() {
        try (InputStream input = PropertyReader.class.getClassLoader().getResourceAsStream(PROPERTIES_FILE)) {
            properties = new Properties();
            properties.load(input);
        } catch (IOException e) {
            throw new RuntimeException(String.format("Error while reading %s: %s", PROPERTIES_FILE, e.getMessage()));
        }
    }

    public Object readProperty(String key) throws RuntimeException {
        Object value = properties.get(key);
        if (value == null) {
            throw new RuntimeException(String.format("Key %s in not" + " found in %s file", key, PROPERTIES_FILE));
        }
        return value;
    }
}
