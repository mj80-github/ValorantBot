package dev.mj80.valorant.valorantbot.discord;

import dev.mj80.valorant.valorantbot.ValorantBot;
import dev.mj80.valorant.valorantcore.messages.Messages;
import dev.mj80.valorant.valorantcore.util.TextUtils;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.Objects;

public class MessageListener extends ListenerAdapter {
    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {
        Server server = ValorantBot.getInstance().getServer();
        if(event.getChannel().getIdLong() == 1053811114934288425L) {
            if(!event.getAuthor().isBot()) {
                String message = event.getMessage().getContentDisplay().startsWith("> say ")
                        ? event.getMessage().getContentDisplay().replaceFirst("> say ", "")
                        : event.getMessage().getContentDisplay();
                if (message.startsWith("> say ") || !message.startsWith("> ")) {
                    server.getOnlinePlayers().forEach(player -> player.sendMessage(String.format(Messages.Misc.DISCORD_MINECRAFT_CHAT.getMessage(),
                            TextUtils.translateAlternateColorCodes('&',
                                    "&#" + Integer.toHexString(Objects.requireNonNull(Objects.requireNonNull(event.getMember()).getColor()).getRGB()).substring(2)
                                            + event.getAuthor().getName()
                            ), message)));
                } else {
                    String command = message.replace("> ", "");
                    server.getScheduler().runTask(ValorantBot.getInstance(), () -> server.dispatchCommand(server.getConsoleSender(), command));
                    server.getOnlinePlayers().stream().filter(player -> player.hasPermission("fpscore.console")).forEach(staff ->
                            staff.sendMessage(String.format(Messages.Misc.DISCORD_CONSOLE_SENT.getMessage(), event.getAuthor().getName()+"#"+event.getAuthor().getDiscriminator(),
                                    command)));
                }
            }
        }
    }
    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        switch (event.getName()) {
            case "link" -> {
                event.deferReply().queue();
                if (event.getOption("code", OptionMapping::getAsString) == null) {
                    event.getHook().editOriginal("**ERROR** `No code specified`").queue();
                } else {
                    Short code;
                    try {
                        code = Objects.requireNonNull(event.getOption("code", OptionMapping::getAsInt)).shortValue();
                    } catch (ArithmeticException exception) {
                        event.getHook().editOriginal("**ERROR** `Not an integer`").queue();
                        break;
                    }
                    Player player = ValorantBot.getInstance().getBot().getLinkCode(code);
                    Member member = event.getMember();
                    assert member != null;
                    if (player != null) {
                        player.sendMessage(Messages.Commands.LINK_LINKING.getMessage());
                        event.getHook().editOriginal("Linking to **" + player.getName() + "**... (`" + player.getUniqueId() + "`)").queue();
                        ValorantBot.getInstance().getBot().removeLinkCode(code);
                        File linkFile = new File(ValorantBot.getInstance().getDataFolder() + File.separator + player.getUniqueId() + File.separator + "discord.txt");
                        linkFile.mkdirs();
                        TextUtils.writeFile(linkFile, Objects.requireNonNull(member).getId());
                        Scoreboard scoreboard = ValorantBot.getInstance().getServer().getScoreboardManager().getMainScoreboard();
                        Objective objective = scoreboard.getObjective("Team");
                        assert objective != null;
                        if (objective.getScore(player).getScore() == 1) {
                            Objects.requireNonNull(event.getGuild()).addRoleToMember(Objects.requireNonNull(member),
                                    Objects.requireNonNull(event.getGuild().getRoleById(1053548525524365355L))).queue();
                        }
                        if (objective.getScore(player).getScore() == 2) {
                            Objects.requireNonNull(event.getGuild()).addRoleToMember(Objects.requireNonNull(member),
                                    Objects.requireNonNull(event.getGuild().getRoleById(1053548525524365355L))).queue();
                        }
                        event.getHook().editOriginal("You are now linked to **" + player.getName() + "**! (`" + player.getUniqueId() + "`)").queue();
                        player.sendMessage(String.format(Messages.Commands.LINK_LINKED.getMessage(), member.getUser().getName() + "#" + member.getUser().getDiscriminator()));
                        
                    } else {
                        event.getHook().editOriginal("**ERROR** `Invalid Code`").queue();
                    }
                }
            }
        }
    }
}
