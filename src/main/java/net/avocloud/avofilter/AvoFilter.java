package net.avocloud.avofilter;

import com.google.inject.Inject;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.plugin.annotation.DataDirectory;
import com.velocitypowered.api.proxy.ProxyServer;
import net.avocloud.avofilter.listener.PlayerChatListener;
import net.avocloud.avofilter.util.ConfigUtil;
import net.avocloud.avofilter.util.FilterUtil;
import net.avocloud.avofilter.util.JsonUtil;
import org.slf4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedHashMap;
import java.util.Objects;

@Plugin(
        id = "avo-filter",
        name = "AvoFilter",
        version = BuildConstants.VERSION,
        description = "AvoFilter client",
        authors = {"Onako2", "Avocloud"},
        url = "https://avocloud.net/"
)
public class AvoFilter {
    
    private final ProxyServer server;
    private final Logger logger;
    private final Path dataDirectory;
    
    @Inject
    public AvoFilter(ProxyServer server, Logger logger, @DataDirectory Path dataDirectory) {
        this.server = server;
        this.logger = logger;
        this.dataDirectory = dataDirectory;
        ConfigUtil.dataDirectory = dataDirectory;
        JsonUtil.logger = logger;
        CacheStuff.server = server;
    }
    
    @Subscribe
    public void onProxyInitialization(ProxyInitializeEvent event) {
        server.getEventManager().register(this, new PlayerChatListener());
        
        
        Path configPath = dataDirectory.resolve("config.yml");
        
        logger.info(configPath.toString());
        
        if (!Files.exists(dataDirectory)) {
            try {
                Files.createDirectories(dataDirectory);
            } catch (IOException e) {
                logger.error("Failed to create directory " + dataDirectory, e);
                return;
            }
        }
        
        //put config.yml from resources to dataDirectory
        if (!Files.exists(configPath)) {
            try (InputStream in = getClass().getClassLoader().getResourceAsStream("config.yml")) {
                if (in == null) {
                    logger.error("Could not find config.yml in resources");
                    return;
                }
                Files.copy(in, configPath);
                logger.info("Copied config.yml to " + dataDirectory);
            } catch (IOException e) {
                logger.error("Failed to copy config.yml to " + dataDirectory, e);
            }
        }
        
        CacheStuff.debug = ConfigUtil.getConfigBoolean("debug");
        CacheStuff.address = ConfigUtil.getConfigString("address");
        CacheStuff.key = ConfigUtil.getConfigString("key");
        CacheStuff.type = (String) ((LinkedHashMap<?, ?>) Objects.requireNonNull(ConfigUtil.getConfigObject("punish"))).get("type");
        CacheStuff.content = (String) ((LinkedHashMap<?, ?>) Objects.requireNonNull(ConfigUtil.getConfigObject("punish"))).get("content");
        
        logger.info("Address: " + CacheStuff.address);
        
        logger.info("AvoFilter initialized");
        
        logger.info(new FilterUtil("Hello!").getRating().name()); // Testing if the filter works with a clean message
        logger.info(new FilterUtil("Hurensohn!").getRating().name()); // Testing if the filter works with a bad message
    }
}
