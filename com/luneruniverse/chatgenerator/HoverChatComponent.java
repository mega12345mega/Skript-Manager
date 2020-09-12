package com.luneruniverse.chatgenerator;

public class HoverChatComponent extends ChatComponent {
    
    private String hover;
    
    public HoverChatComponent(String hover) {
        this.hover = escape(hover);
    }
    
    public String getCommand() {
        return hover;
    }
    
    
    @Override
    public String toString() {
        return "\"hoverEvent\":{\"action\":\"show_text\",\"value\":\"" + hover + "\"}";
    }
    
}
