package com.luneruniverse.commands;

import java.util.List;
import org.bukkit.command.CommandSender;

public interface CommandReciver {
    
    public boolean recive(CommandSender sender, String label, String[] args);
    
    public List<String> onTabComplete(CommandSender sender, String label, String[] args);
    
    public String getUsage();
    
    public String getDescription();
    
    public List<String> getAliases();
    
    public String getExtendedHelp();
    
    public String getPermission();
    
}
