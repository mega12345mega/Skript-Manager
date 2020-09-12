package com.luneruniverse.chatgenerator;

public class CopyChatComponent extends ChatComponent {
    
    private String cmd;
    
    public CopyChatComponent(String cmd) {
        this.cmd = escape(cmd);
    }
    
    public String getCommand() {
        return cmd;
    }
    
    
    @Override
    public String toString() {
        return "\"clickEvent\":{\"action\":\"copy_to_clipboard\",\"value\":\"" + cmd + "\"}";
    }
    
}
