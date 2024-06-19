package dev.cognitivity.valorant.valorantbot;

import dev.cognitivity.valorant.valorantbot.discord.Bot;
import dev.cognitivity.valorant.valorantbot.managers.ModalManager;
import dev.cognitivity.valorant.valorantbot.managers.CommandManager;
import io.github.cdimascio.dotenv.Dotenv;
import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Level;

public final class ValorantBot extends JavaPlugin {
    @Getter private static ValorantBot instance;
    @Getter private Bot bot;
    @Getter private CommandManager commandManager;
    @Getter private ModalManager modalManager;
    
    @Override
    public void onEnable() {
        instance = this;
        commandManager = new CommandManager();
        modalManager = new ModalManager();
        getLogger().log(Level.INFO, "Starting bot...");
        bot = new Bot(Dotenv.configure().load().get("TOKEN"));
        bot.build();
        getLogger().log(Level.INFO, "ValorantBot has been enabled.");
    }
    
    @Override
    public void onDisable() {
//        Guild guild = bot.getGuild();
//        TextChannel textChannel = guild.getTextChannelById(1229178993769582612L);
//
//        EmbedBuilder eb = new EmbedBuilder();
//        eb.setColor(0x00aaff);
//        eb.setTitle("SERVER APPEALS");
//        eb.setThumbnail("https://cdn.discordapp.com/avatars/1053787916347908136/c7f19f5a0ac7b249205714634ef86bb8.png");
//        eb.setDescription("""
//                Click on the button below to create an appeal. Proof of your innocence is heavily recommended.
//                Do not share your penalty ID or ping staff to check your appeals. The staff member who issued the penalty will be notified of the appeal first.
//                Once a staff member replies to your appeal, you may reasonably ping them in replies.
//
//                ### MUTES
//                Minecraft logs are not considered sufficient evidence. Chat logs from the server relating to the penalty may be provided.
//
//                ### BANS
//                Sending a list of mods or files is not considered sufficient evidence. Admitting to cheating will not result in an unban.
//                """);
//
//        eb.setFooter("Appeals are typically replied within 24-48 hours; do not ping staff to check your appeal.");
//
//        Button appealButton = Button.danger("appeals.create", "CREATE APPEAL");
//        textChannel.sendMessageEmbeds(eb.build()).addActionRow(appealButton).queue();
        getLogger().log(Level.INFO, "Stopping bot...");
        bot.getJda().shutdown();
        getLogger().log(Level.INFO, "ValorantBot has been disabled.");
    }
}
