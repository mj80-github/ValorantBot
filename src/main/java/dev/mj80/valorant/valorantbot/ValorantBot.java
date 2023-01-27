package dev.mj80.valorant.valorantbot;

import dev.mj80.valorant.valorantbot.discord.Bot;
import io.github.cdimascio.dotenv.Dotenv;
import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;

public final class ValorantBot extends JavaPlugin {
    @Getter private static ValorantBot instance;
    @Getter private Bot bot;
    
    @Override
    public void onEnable() {
        instance = this;
        bot = new Bot(Dotenv.configure().load().get("TOKEN"));
        bot.build();
    }
    
    @Override
    public void onDisable() {}
}
