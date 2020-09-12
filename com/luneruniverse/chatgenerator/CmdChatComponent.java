package com.luneruniverse.chatgenerator;

public class CmdChatComponent extends ChatComponent {
    
    private String cmd;
    
    public CmdChatComponent(String cmd) {
        this.cmd = escape(cmd);
    }
    
    public String getCommand() {
        return cmd;
    }
    
    
    @Override
    public String toString() {
        return "\"clickEvent\":{\"action\":\"run_command\",\"value\":\"" + cmd + "\"}";
    }
    
}
