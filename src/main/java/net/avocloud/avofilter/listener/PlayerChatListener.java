package net.avocloud.avofilter.listener;

import com.velocitypowered.api.event.PostOrder;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.player.PlayerChatEvent;
import com.velocitypowered.api.proxy.Player;
import net.avocloud.avofilter.CacheStuff;
import net.avocloud.avofilter.ChatFilterCategory;
import net.avocloud.avofilter.util.FilterUtil;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.minimessage.MiniMessage;

public class PlayerChatListener {
    
    private TextComponent getMessageComponent(ChatFilterCategory rating) {
        return Component.text()
                .color(NamedTextColor.DARK_RED)
                .content("Your message couldn't be delivered! ")
                .append(Component.text("Reason: "))
                .append(Component.text(rating.toString(), NamedTextColor.RED))
                .build();
    }
    
    
    @Subscribe(order = PostOrder.NORMAL)
    public void onPlayerChatMessage(PlayerChatEvent event) {
        Player player = event.getPlayer();
        
        if (player.hasPermission("avofilter.bypass")) {
            return;
        }
        
        ChatFilterCategory rating = new FilterUtil(event.getMessage()).getRating();
        if (rating != ChatFilterCategory.FINE) {
            
            String serializedContent = CacheStuff.content.replace("%player%", player.getUsername()).replace("%player_uuid%", player.getUniqueId().toString()).replace("%rating%", rating.toString()).replace("%message%", event.getMessage());
            switch (CacheStuff.type) {
                case "kick":
                    player.disconnect(MiniMessage.miniMessage().deserialize(serializedContent));
                    return;
                case "command":
                    CacheStuff.server.getCommandManager().executeAsync(CacheStuff.server.getConsoleCommandSource(), serializedContent);
                    break;
                case "cancel":
                    player.sendMessage(MiniMessage.miniMessage().deserialize(serializedContent));
                    event.setResult(PlayerChatEvent.ChatResult.denied());
                    break;
                default:
                    player.sendMessage(getMessageComponent(rating));
                    break;
            }
        }
    }
    
}
