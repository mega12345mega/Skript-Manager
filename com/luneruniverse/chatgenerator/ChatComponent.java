package com.luneruniverse.chatgenerator;

public abstract class ChatComponent {
    
    private ChatComponent next;
    
    public ChatComponent() {
        
    }
    
    public ChatComponent append(ChatComponent next) {
        this.next = next;
        return next;
    }
    
    public ChatComponent getNext() {
        return next;
    }
    
    
    @Override
    public abstract String toString();
    
    
    protected static String escape(String text) {
        return text.replace("\\", "\\\\").replace("\"", "\\\"");
    }
    
}
