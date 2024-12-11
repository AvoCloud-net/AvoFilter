package net.avocloud.avofilter.util;

import net.avocloud.avofilter.CacheStuff;
import net.avocloud.avofilter.ChatFilterCategory;

public class FilterUtil {
    private final String chatMessage;
    
    public FilterUtil(String chatMessage) {
        this.chatMessage = chatMessage;
    }
    
    public ChatFilterCategory getRating() {
        String apiResponse = Util.httpPost(CacheStuff.address, "AvoFilter (AvoCloud)", String.format("{\"key\": \"%s\",\"message\": \"%s\"}", CacheStuff.key, chatMessage));
        
        System.out.println(apiResponse);
        
        if (apiResponse == null) {
            return ChatFilterCategory.FINE;
        }
        
        if (apiResponse.equals("{\"error\":\"access denied\"}")) {
            throw new RuntimeException("Access denied. Please check your API key.");
        }
        
        if (apiResponse.equals("{}")) {
            return ChatFilterCategory.FINE;
        }
        return ChatFilterCategory.MATCH;
    }
    
    public boolean isAllowed() {
        return getRating() == ChatFilterCategory.FINE;
    }
}
