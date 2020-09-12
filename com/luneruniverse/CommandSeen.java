package com.luneruniverse;

import com.luneruniverse.commands.CommandReciver;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.BukkitCommand;

/**
* From: https://www.spigotmc.org/threads/small-easy-register-command-without-plugin-yml.38036/
* (Edited)
*/
public class CommandSeen extends BukkitCommand {
    CommandReciver reciver;
    
    public CommandSeen(String name, CommandReciver reciver) {
        super(name);
        this.description = reciver.getDescription();
        this.usageMessage = reciver.getUsage();
        this.setPermission(reciver.getPermission());
        this.setAliases(reciver.getAliases());
        
        this.reciver = reciver;
    }
    
    @Override
    public boolean execute(CommandSender sender, String alias, String[] args) {
        if (!sender.hasPermission(this.getPermission())) {
            sender.sendMessage(ChatColor.RED + "You don't have permission to run this command.");
            return true;
        }
        
        return reciver.recive(sender, alias, args);
    }
    
    @Override
    public List<String> tabComplete(CommandSender sender, String alias, String[] args) {
        return reciver.onTabComplete(sender, alias, args);
    }
}