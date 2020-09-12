package com.luneruniverse.chatgenerator;

public class TextChatComponent extends ChatComponent {
    
    private String text;
    private final boolean esc;
    
    public TextChatComponent(String text, boolean esc) {
        this.text = text;
        this.esc = esc;
    }
    public TextChatComponent(String text) {
        this(text, true);
    }
    
    public String getText() {
        return text;
    }
    public boolean isEscaped() {
        return esc;
    }
    
    
    @Override
    public String toString() {
        return "\"text\":\"" + (esc ? escape(text) : text) + "\"";
    }
    
}
