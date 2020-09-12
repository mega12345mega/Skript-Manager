package com.luneruniverse.chatgenerator;

public class SuggestCmdChatComponent extends ChatComponent {
    
    private String cmd;
    
    public SuggestCmdChatComponent(String cmd) {
        this.cmd = escape(cmd);
    }
    
    public String getCommand() {
        return cmd;
    }
    
    
    @Override
    public String toString() {
        return "\"clickEvent\":{\"action\":\"suggest_command\",\"value\":\"" + cmd + "\"}";
    }
    
}
