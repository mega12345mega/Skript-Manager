package com.luneruniverse.chatgenerator;

import java.util.ArrayList;
import net.minecraft.server.v1_16_R2.ChatMessageType;
import net.minecraft.server.v1_16_R2.IChatBaseComponent;
import net.minecraft.server.v1_16_R2.Packet;
import net.minecraft.server.v1_16_R2.PacketPlayOutChat;
import org.bukkit.craftbukkit.v1_16_R2.entity.CraftPlayer;
import org.bukkit.entity.Player;

public class ChatGenerator {
    
    private ArrayList<ArrayList<ChatComponent>> parts;
    
    public ChatGenerator() {
        parts = new ArrayList<>();
        nextPart();
    }
    
    public ChatGenerator addComponent(ChatComponent component) {
        parts.get(parts.size()-1).add(component);
        return this;
    }
    public ChatGenerator nextPart() {
        parts.add(new ArrayList<>());
        return this;
    }
    
    
    @Override
    public String toString() {
        ArrayList<String> extra = new ArrayList<>();
        for (ArrayList<ChatComponent> part : parts) {
            StringBuilder builder = new StringBuilder();
            builder.append("{");
            for (ChatComponent component : part) {
                if (builder.length() > 1)
                    builder.append(",");
                builder.append(component.toString());
            }
            builder.append("}");
            extra.add(builder.toString());
        }
        
        return "{\"text\":\"\",\"extra\":" + extra + "}";
    }
    
    
    public void sendMessage(Player player) {
        Packet packet = new PacketPlayOutChat(IChatBaseComponent.ChatSerializer.a(toString()), ChatMessageType.CHAT, player.getUniqueId());
        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);
    }
    
}
