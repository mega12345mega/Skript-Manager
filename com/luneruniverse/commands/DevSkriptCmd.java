package com.luneruniverse.commands;

import com.luneruniverse.chatgenerator.CopyChatComponent;
import com.luneruniverse.chatgenerator.HoverChatComponent;
import com.luneruniverse.chatgenerator.CmdChatComponent;
import com.luneruniverse.chatgenerator.ChatGenerator;
import com.luneruniverse.chatgenerator.SuggestCmdChatComponent;
import com.luneruniverse.chatgenerator.TextChatComponent;
import com.luneruniverse.SkriptManager;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;

public class DevSkriptCmd implements CommandReciver {
    
    private SkriptManager main;
    
    public DevSkriptCmd(SkriptManager main) {
        this.main = main;
    }
    
    @Override
    public boolean recive(CommandSender sender, String label, String[] args) {
        
        boolean dev = false;
        
        if (sender instanceof ConsoleCommandSender)
            dev = true;
        
        else if (sender instanceof Player)
            if (main.isDev((Player)sender))
                dev = true;
        
        if (dev) {
            
            if (args.length == 1) {
                if (args[0].equals("list")) {
                    for (String filename : getFiles())
                        if (sender instanceof Player)
                            new ChatGenerator().addComponent(new TextChatComponent(filename)).addComponent(new SuggestCmdChatComponent("/devskript view " + filename.replace("\\", "\\\\"))).addComponent(new HoverChatComponent("View file contents")).sendMessage((Player) sender);
                        else
                            sender.sendMessage(filename);
                    return true;
                } else if (args[0].equals("help")) {
                    sender.sendMessage(getExtendedHelp());
                    return true;
                }
            }
            
            if (args.length < 2 || args.length > 3) {
                sender.sendMessage(getUsage());
            } else {
                if (!args[0].equals("view") && (args[0].equals("edit") || args[0].equals("rename")) == (args.length != 3)) {
                    sender.sendMessage(getUsage());
                    return true;
                }
                
                String filename = args[1];
                if (!filename.endsWith(".sk"))
                    filename += ".sk";
                if (filename.indexOf(".") != filename.lastIndexOf(".")) {
                    sender.sendMessage("Only (optional) .sk extension allowed");
                    return true;
                }
                File file = new File("plugins/Skript/scripts/" + filename);
                switch (args[0]) {
                    case "create":
                        if (file.exists())
                            sender.sendMessage("File already exists. Nothing changed.");
                        else {
                            try {
                                if (!file.getParentFile().exists())
                                    file.getParentFile().mkdirs();
                                file.createNewFile();
                                sender.sendMessage("File created successfully!");
                            } catch (IOException e) {
                                sender.sendMessage("An error occurred creating the file");
                            }
                        }
                        break;
                    case "delete":
                    case "remove":
                        if (file.exists()) {
                            file.delete();
                            sender.sendMessage("File deleted successfully!");
                        } else
                            sender.sendMessage("File doesn't exist. Nothing changed.");
                        break;
                    case "edit":
                        if (file.exists()) {
                            try {
                                String contents = fetchNewFile(sender, args[2]);
                                if (contents != null) {
                                    try {
                                        FileOutputStream output = new FileOutputStream(file);
                                        output.write(contents.getBytes());
                                        output.close();
                                        
                                        sender.sendMessage("File edited successfully!");
                                    } catch (IOException e) {
                                        sender.sendMessage("Error editing file");
                                    }
                                }
                            } catch (IOException e) {
                                sender.sendMessage("Error fetching file");
                            }
                        } else
                            sender.sendMessage("File doesn't exist");
                        break;
                    case "view":
                        int page = 1;
                        if (args.length == 3) {
                            try {
                                page = Integer.parseInt(args[2]);
                            } catch (NumberFormatException e) {
                                sender.sendMessage(args[2] + " isn't a valid number");
                                return true;
                            }
                        }
                        if (file.exists()) {
                            try {
                                getPage(sender, filename, new String(Files.readAllBytes(file.toPath())), page);
                            } catch (IOException e) {
                                sender.sendMessage("Error reading file");
                            }
                        } else
                            sender.sendMessage("File doesn't exist");
                        break;
                    case "copy-file-contents":
                        if (sender instanceof Player) {
                            if (file.exists()) {
                                try {
                                    String contents = new String(Files.readAllBytes(file.toPath()));
                                    new ChatGenerator().addComponent(new TextChatComponent(ChatColor.GOLD + "" + ChatColor.UNDERLINE + "Click here" + ChatColor.RESET)).addComponent(new CopyChatComponent(contents)).nextPart().addComponent(new TextChatComponent(" to copy the file contents to your clipboard")).sendMessage((Player) sender);
                                } catch (IOException e) {
                                    sender.sendMessage("Error reading file");
                                }
                            } else
                                sender.sendMessage("File doesn't exist");
                        } else
                            sender.sendMessage("copy-file-contents only works for players. Use /devskript view and copy directly from the console instead");
                        break;
                    case "rename":
                        String targetname = args[2];
                        if (!targetname.endsWith(".sk"))
                            targetname += ".sk";
                        if (targetname.indexOf(".") != targetname.lastIndexOf(".")) {
                            sender.sendMessage("Only (optional) .sk extension allowed");
                            return true;
                        }
                        File target = new File("plugins/Skript/scripts/" + targetname);
                        if (!target.getParentFile().exists())
                            target.getParentFile().mkdirs();
                        
                        if (file.exists()) {
                            if (file.renameTo(target))
                                sender.sendMessage("File renamed successfully");
                            else
                                sender.sendMessage("Error renaming file");
                        } else
                            sender.sendMessage("File doesn't exist");
                        break;
                    default:
                        sender.sendMessage(getUsage());
                }
            }
            
        } else
            sender.sendMessage("You do not have permission to use this command.");
        
        return true;
        
    }
    
    private List<String> getFiles(List<String> list, File parent) {
        for (File child : parent.listFiles())
            if (child.isDirectory())
                getFiles(list, child);
            else
                list.add(child.getPath().substring("plugins/Skript/scripts/".length()));
        return list;
    }
    private List<String> getFiles() {
        return getFiles(new ArrayList<>(), new File("plugins/Skript/scripts/"));
    }
    
    private String fetchNewFile(CommandSender sender, String url) throws IOException {
        
        if (url.equals("-book")) {
            if (sender instanceof Player) {
                Player player = (Player) sender;
                ItemStack book = player.getInventory().getItemInMainHand();
                if (book.getType() == Material.WRITABLE_BOOK || book.getType() == Material.WRITTEN_BOOK) {
                    BookMeta meta = (BookMeta) book.getItemMeta();
                    StringBuilder content = new StringBuilder();
                    for (String page : meta.getPages())
                        content.append(page);
                    return content.toString();
                } else {
                    sender.sendMessage("You must be holding a writen / writable book");
                    return null;
                }
            } else {
                sender.sendMessage("Only players can use the -book flag");
                return null;
            }
        }
        
        if (!url.startsWith("https://"))
            url = "https://" + url;
        if (!url.startsWith("https://paste.helpch.at/raw/")) {
            sender.sendMessage("You must use paste.helpch.at, and make sure the url refers to the raw text");
            return null;
        }
        
        String content = null;
        URLConnection connection = new URL(url).openConnection();
        
        Scanner scanner = new Scanner(connection.getInputStream());
        scanner.useDelimiter("\\Z");
        content = scanner.next();
        scanner.close();
        
        return content;
        
    }
    
    private static final int PAGE_SIZE = 50;
    private void getPage(CommandSender sender, String filename, String text, int page) {
        
        if (sender instanceof Player) {
            Player player  = (Player) sender;
            
            if (page < 1) {
                sender.sendMessage("The page must be at least 1");
                return;
            }
            String[] lines = text.split("\n");
            int numPages = (int) Math.ceil((double)lines.length / PAGE_SIZE);
            if (page > numPages) {
                sender.sendMessage("There are only " + numPages + " pages");
                return;
            }
            
            sender.sendMessage("File contents of " + filename + " (page " + page + "):");
            for (int i = 0; i < PAGE_SIZE; i++) {
                int line = i + PAGE_SIZE * (page - 1);
                if (line >= lines.length)
                    break;
                sender.sendMessage(ChatColor.GOLD + "(" + (line + 1) + ") " + ChatColor.RESET + lines[line].replace("\t", "    "));
            }
            if (page > 1)
                new ChatGenerator().addComponent(new TextChatComponent(ChatColor.GRAY + "" + ChatColor.BOLD + "Previous Page")).addComponent(new CmdChatComponent("/devskript view " + filename + " " + (page - 1))).sendMessage(player);
            if (page < numPages)
                new ChatGenerator().addComponent(new TextChatComponent(ChatColor.GRAY + "" + ChatColor.BOLD + "Next Page")).addComponent(new CmdChatComponent("/devskript view " + filename + " " + (page + 1))).sendMessage(player);
        } else {
            sender.sendMessage("File contents of " + filename + ":");
            sender.sendMessage(text);
        }
        
    }
    
    
    
    @Override
    public List<String> onTabComplete(CommandSender sender, String label, String[] args) {
        if (args.length == 1) {
            ArrayList<String> list = new ArrayList<>(Arrays.asList("help", "create", "remove", "delete", "edit", "view", "rename", "copy-file-contents", "list"));
            list.removeIf((item) -> { return !item.startsWith(args[0]); });
            
            return list;
        } else if (args.length == 2) {
            switch (args[0]) {
                case "remove":
                case "delete":
                case "edit":
                case "view":
                case "rename":
                case "copy-file-contents":
                    List<String> list = getFiles();
                    list.removeIf((item) -> { return !item.startsWith(args[1]); });
                    
                    return list;
                default:
                    return new ArrayList<>();
            }
        }
        
        return new ArrayList<>();
    }
    
    @Override
    public String getUsage() {
        return "/devskript (create | remove/delete | view | copy-file-contents) <filename>\n/devskript edit <filename> (<paste.helpch.at raw url> | -book)\n/devskript rename <filename> <new filename>\n/devskript (list | help)";
    }
    
    @Override
    public String getDescription() {
        return "Manage skript files from within game";
    }
    
    @Override
    public List<String> getAliases() {
        return new ArrayList<>();
    }
    
    @Override
    public String getExtendedHelp() {
        return ("Help: /devskript\n"
                + "\tcreate <filename> - Create a skript file\n"
                + "\tremove/delete <filename> - Delete a skript file\n"
                + "\tview <filename> [page] - View a skript file 50 lines at a time; page defaults to 1\n"
                + "\tedit <filename> <url> - Set the file contents to the contents of the url; use paste.helpch.at's raw text feature\n"
                + "\tedit <filename> -book - Set the file contents to the contents of a book your holding\n"
                + "\trename <filename> <new filename> - Rename a skript file\n"
                + "\tcopy-file-contents <filename> - Copy the contents of a file to your clipboard\n"
                + "\tlist - List all the skript files\n"
                + "\thelp - Display this message\n"
                + " \n"
                + "This program supports subfolders, but not file paths with spaces, so \"folder/test.sk\" is ok, while "
                + "\"test program.sk\" won't work\n"
                + "------------\n"
                + "Created by mega12345mega").replace("\t", "    ");
    }
    
    @Override
    public String getPermission() {
        return "skriptmanager.manage";
    }
    
}
