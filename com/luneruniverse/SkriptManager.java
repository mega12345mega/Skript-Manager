package com.luneruniverse;

import com.luneruniverse.commands.DevSkriptCmd;
import com.luneruniverse.commands.ManageDevsCmd;
import com.luneruniverse.commands.CommandReciver;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandMap;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class SkriptManager extends JavaPlugin {
    
    public static final Logger log = Logger.getLogger("Minecraft");
    private List<String> devs;
    
    private Map<String, CommandReciver> cmds;
    
    @Override
    public void onEnable() {
        
        saveDefaultConfig();
        devs = (List<String>) getConfig().getList("devs");
        if (devs == null)
            devs = new ArrayList<>();
        
        
        
        Bukkit.getServer().getPluginManager().registerEvents(new SkriptManagerEventHandler(), this);
        
        cmds = new HashMap<>();
        cmds.put("devskript", new DevSkriptCmd(this));
        cmds.put("managedevs", new ManageDevsCmd(this));
        
        try {
            
            Field bukkitCmdMap = Bukkit.getServer().getClass().getDeclaredField("commandMap");
            
            bukkitCmdMap.setAccessible(true);
            CommandMap cmdMap = (CommandMap) bukkitCmdMap.get(Bukkit.getServer());
            
            for (String cmd : cmds.keySet())
                cmdMap.register(cmd, new CommandSeen(cmd, cmds.get(cmd)));
            
        } catch (IllegalAccessException | IllegalArgumentException | NoSuchFieldException | SecurityException e) {
            
            log.log(Level.SEVERE, e.getMessage());
            Bukkit.getServer().shutdown();
            
        }
        
    }
    
    
    
    public boolean isDev(Player player) {
        return devs.contains(player.getUniqueId().toString());
    }
    public void addDev(Player player) {
        if (!isDev(player)) {
            devs.add(player.getUniqueId().toString());
            getConfig().set("devs", devs);
            saveConfig();
        }
    }
    public void removeDev(Player player) {
        if (isDev(player)) {
            devs.remove(player.getUniqueId().toString());
            getConfig().set("devs", devs);
            saveConfig();
        }
    }
    public void listDevs(CommandSender sender) {
        ArrayList<String> devs = new ArrayList<>();
        for (String dev : this.devs)
            devs.add(Bukkit.getOfflinePlayer(UUID.fromString(dev)).getName());
        sender.sendMessage(devs.toString());
    }
    
}