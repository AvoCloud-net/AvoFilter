package net.avocloud.avofilter.util;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import org.slf4j.Logger;

public class JsonUtil {
    private final String json;
    public static Logger logger;
    
    public JsonUtil(String json) {
        this.json = json;
    }
    
    public Object getObject() {
        Gson gson = new Gson();
        
        try {
            return gson.fromJson(json, Object.class);
        } catch (JsonSyntaxException e) {
            logger.error("JSON Syntax Error: " + e.getMessage());
            logger.debug("Failed JSON: " + json);
            return null;
        }
    }
}
