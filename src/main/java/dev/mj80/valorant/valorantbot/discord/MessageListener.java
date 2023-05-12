package dev.mj80.valorant.valorantbot.discord;

import dev.mj80.valorant.valorantbot.utils.CoreUtils;
import dev.mj80.valorant.valorantbot.Messages;
import dev.mj80.valorant.valorantbot.ValorantBot;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.bukkit.Server;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.logging.Level;

public class MessageListener extends ListenerAdapter {
    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {
        Server server = ValorantBot.getInstance().getServer();
        event.getMessage().getJumpUrl();
        if(event.getChannel().getIdLong() == 1053811114934288425L) {
            if(!event.getAuthor().isBot()) {
                String message = event.getMessage().getContentDisplay().startsWith("> say ")
                        ? event.getMessage().getContentDisplay().replaceFirst("> say ", "")
                        : event.getMessage().getContentDisplay();
                if (message.startsWith("> say ") || !message.startsWith("> ")) {
                    server.getOnlinePlayers().forEach(player -> player.sendMessage(CoreUtils.translateAlternateColorCodes('&',
                            Messages.DISCORD_MINECRAFT_CHAT.getMessage("&#" + Integer.toHexString(Objects.requireNonNull(
                                    Objects.requireNonNull(event.getMember()).getColor()).getRGB()).substring(2) + event.getAuthor().getName(), message))));
                } else {
                    String command = message.replace("> ", "");
                    server.getScheduler().runTask(ValorantBot.getInstance(), () -> server.dispatchCommand(server.getConsoleSender(), command));
                    server.getOnlinePlayers().stream().filter(player -> player.hasPermission("fpscore.console")).forEach(staff ->
                            staff.sendMessage(CoreUtils.translateAlternateColorCodes('&',
                                    Messages.DISCORD_CONSOLE_SENT.getMessage(event.getAuthor().getName() + "#" + event.getAuthor().getDiscriminator(), command))));
                }
            }
        }
    }
    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
        ValorantBot.getInstance().getCommandManager().getCommands().stream().filter(command -> event.getName().equals(command.getCommandData().getName()))
                .forEach(command -> {
                    long start = System.currentTimeMillis();
                    User user = Objects.requireNonNull(event.getMember()).getUser();
                    ValorantBot.getInstance().getLogger().log(Level.INFO, "Running command \"/"+event.getName()+"\" for "+ user.getName() + "#" +
                            user.getDiscriminator() + " in #" + event.getChannel().getName()+"...");
                    command.run(event);
                    ValorantBot.getInstance().getLogger().log(Level.INFO, "Successfully ran command \"/"+event.getName()+"\" for "+ user.getName() +
                            "#" + user.getDiscriminator() + " in #" + event.getChannel().getName()+"! Finished in "+(System.currentTimeMillis()-start)+" ms.");
                });
    }
}
