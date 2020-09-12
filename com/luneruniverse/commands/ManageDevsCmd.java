package com.luneruniverse.commands;

import com.luneruniverse.SkriptManager;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

public class ManageDevsCmd implements CommandReciver {
    
    private SkriptManager main;
    
    public ManageDevsCmd(SkriptManager main) {
        this.main = main;
    }
    
    @Override
    public boolean recive(CommandSender sender, String label, String[] args) {
        
        if (args.length > 0 && args[0].equals("list") && sender instanceof Player && main.isDev((Player) sender)) {
            main.listDevs(sender);
            return true;
        }
        
        if (sender instanceof ConsoleCommandSender) {
            
            if (args.length > 0) {
                
                switch (args[0]) {
                    case "add":
                        if (args.length != 2)
                            sender.sendMessage(getUsage());
                        else
                            main.addDev(Bukkit.getPlayer(args[1]));
                        sender.sendMessage(args[1] + " is now a dev");
                        break;
                    case "remove":
                        if (args.length != 2)
                            sender.sendMessage(getUsage());
                        else
                            main.removeDev(Bukkit.getPlayer(args[1]));
                        sender.sendMessage(args[1] + " is no longer a dev");
                        break;
                    case "list":
                        if (args.length != 1)
                            sender.sendMessage(getUsage());
                        else
                            main.listDevs(sender);
                        break;
                    default:
                        sender.sendMessage(getUsage());
                }
                
            } else
                sender.sendMessage(getUsage());
            
        } else
            sender.sendMessage("You do not have permission to use this command.");
        
        return true;
        
    }
    
    @Override
    public List<String> onTabComplete(CommandSender sender, String label, String[] args) {
        if (sender instanceof ConsoleCommandSender) {
            if (args.length == 1) {
                ArrayList<String> list = new ArrayList<>(Arrays.asList("add", "remove", "list"));
                list.removeIf((item) -> { return !item.startsWith(args[0]); });

                return list;
            } else if (args.length == 2) {
                switch (args[0]) {
                    case "add":
                    case "remove":
                        ArrayList<? extends Player> players = new ArrayList<>(Bukkit.getOnlinePlayers());
                        players.removeIf((player) -> { return !player.getName().startsWith(args[1]); });

                        ArrayList<String> list = new ArrayList<>();
                        for (Player player : players)
                            list.add(player.getName());

                        return list;
                }
            }
        } else if (args.length == 1 && "list".startsWith(args[0]) && sender instanceof Player && main.isDev((Player) sender))
            return Arrays.asList("list");
        
        return new ArrayList<>();
    }
    
    @Override
    public String getUsage() {
        return "/managedevs ((add | remove) <player> | list)";
    }
    
    @Override
    public String getDescription() {
        return "Manage the skript developers";
    }
    
    @Override
    public List<String> getAliases() {
        return new ArrayList<>();
    }
    
    @Override
    public String getExtendedHelp() {
        return getUsage();
    }
    
    @Override
    public String getPermission() {
        return "skriptmanager.managedevs";
    }
    
}
