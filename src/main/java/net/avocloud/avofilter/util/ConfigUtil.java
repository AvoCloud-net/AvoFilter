package net.avocloud.avofilter.util;

import net.avocloud.avofilter.CacheStuff;
import org.slf4j.Logger;
import org.yaml.snakeyaml.Yaml;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

public class ConfigUtil {
    
    public static Logger logger;
    public static Path dataDirectory;
    
    static Yaml yaml = new Yaml();
    
    static InputStream inputStream = null;
    
    public static String getConfigString(String input) {
        return (String) getConfigObject(input);
    }
    
    public static boolean getConfigBoolean(String input) {
        return (boolean) getConfigObject(input);
    }
    
    public static Object getConfigObject(String input) {
        Path configPath = dataDirectory.resolve("config.yml");
        
        try {
            inputStream = Files.newInputStream(configPath);
            Map<String, Object> obj = yaml.load(inputStream);
            if (CacheStuff.debug) {
                System.out.println(obj);
            }
            return obj.get(input);
        } catch (IOException e) {
            logger.error("Failed to load config.yml from " + dataDirectory, e);
            return null;
        } finally {
            // Close the InputStream in a finally block
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    logger.error("Failed to close InputStream", e);
                }
            }
        }
    }
}
