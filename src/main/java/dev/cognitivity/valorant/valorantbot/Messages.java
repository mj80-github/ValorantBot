package dev.cognitivity.valorant.valorantbot;

import org.jetbrains.annotations.NotNull;

public enum Messages {
    DISCORD_MINECRAFT_CHAT("&#5865F2&lDISCORD &r%s&r: %s"),
    DISCORD_CONSOLE_SENT("<GREEN>&lCONSOLE <GRAY>%s has sent a command from discord: <DARKGREEN>%s"),
    LINK_LINKING("<GRAY>Attempting to link your Discord account..."),
    LINK_LINKED("<GRAY>You are now linked to &#5865F2%s<GRAY>."),
    ;
    
    private final String message;
    
    Messages(@NotNull String message) {
        this.message = message;
    }
    
    public @NotNull String getMessage(Object... args) {
        return String.format(message, args);
    }
    public @NotNull String getMessage() {
        return message;
    }
}
