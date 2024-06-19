package dev.cognitivity.valorant.valorantbot.commands.impl;

import com.google.gson.JsonObject;
import dev.cognitivity.valorant.valorantbot.Messages;
import dev.cognitivity.valorant.valorantbot.commands.DiscordCommand;
import dev.cognitivity.valorant.valorantbot.utils.CoreUtils;
import dev.cognitivity.valorant.valorantdata.ValorantData;
import dev.cognitivity.valorant.valorantdata.DataUtils;
import dev.cognitivity.valorant.valorantdata.ValorantData;
import lombok.Getter;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;

import java.io.File;
import java.util.Objects;

public class Link extends DiscordCommand {
    @Getter private final SlashCommandData commandData =
            Commands.slash("link", "Links your Minecraft account to your Discord account.")
            .setGuildOnly(true)
            .addOption(OptionType.INTEGER, "code", "Link code from \"/link\" in-game.");
    
    
    @Override
    public void run(SlashCommandInteractionEvent event) {
        event.deferReply().queue();
        if (event.getOption("code", OptionMapping::getAsString) == null) {
            event.getHook().editOriginal("**ERROR** `No code specified`").queue();
            return;
        }
        
        Short code;
        try {
            code = Objects.requireNonNull(event.getOption("code", OptionMapping::getAsInt)).shortValue();
        } catch (ArithmeticException exception) {
            event.getHook().editOriginal("**ERROR** `Not an integer`").queue();
            return;
        }
        Player player = instance.getBot().getLinkCode(code);
        Member member = event.getMember();
        assert member != null;
        if(player == null) {
            event.getHook().editOriginal("**ERROR** `Invalid Code`").queue();
            return;
        }
        player.sendMessage(CoreUtils.translateAlternateColorCodes('&', Messages.LINK_LINKING.getMessage()));
        event.getHook().editOriginal("Linking to **" + player.getName() + "**... (`" + player.getUniqueId() + "`)").queue();
        instance.getBot().removeLinkCode(code);
        ValorantData.getInstance().getData(player).getStats().setDiscordId(member.getIdLong());
        Scoreboard scoreboard = instance.getServer().getScoreboardManager().getMainScoreboard();
        Objective objective = scoreboard.getObjective("Team");
        if(objective != null) {
            if (objective.getScore(player).getScore() == 1) {
                Objects.requireNonNull(event.getGuild()).addRoleToMember(Objects.requireNonNull(member),
                        Objects.requireNonNull(event.getGuild().getRoleById(1053548525524365355L))).queue();
            }
            if (objective.getScore(player).getScore() == 2) {
                Objects.requireNonNull(event.getGuild()).addRoleToMember(Objects.requireNonNull(member),
                        Objects.requireNonNull(event.getGuild().getRoleById(1053548525524365355L))).queue();
            }
        }
        ValorantData.getInstance().getData(player).saveData();
        
        JsonObject discordData = new JsonObject();
        discordData.addProperty("link-uuid", player.getUniqueId().toString());
        
        
        File file = new File(ValorantData.getInstance().getDataFolder().getAbsolutePath() + File.separator + "discord" + File.separator + member.getId() + ".json");
        DataUtils.createFile(file);
        DataUtils.writeJSONObject(file, discordData);
        
        event.getHook().editOriginal("You are now linked to **" + player.getName() + "**! (`" + player.getUniqueId() + "`)").queue();
        player.sendMessage(CoreUtils.translateAlternateColorCodes('&',
                Messages.LINK_LINKED.getMessage(member.getUser().getName() + "#" + member.getUser().getDiscriminator())));
    }
}
