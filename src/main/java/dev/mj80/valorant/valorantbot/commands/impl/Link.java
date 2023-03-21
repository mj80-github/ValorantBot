package dev.mj80.valorant.valorantbot.commands.impl;

import dev.mj80.valorant.valorantbot.CoreUtils;
import dev.mj80.valorant.valorantbot.Messages;
import dev.mj80.valorant.valorantbot.ValorantBot;
import dev.mj80.valorant.valorantbot.commands.DiscordCommand;
import dev.mj80.valorant.valorantdata.ValorantData;
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
        Player player = ValorantBot.getInstance().getBot().getLinkCode(code);
        Member member = event.getMember();
        assert member != null;
        if(player == null) {
            event.getHook().editOriginal("**ERROR** `Invalid Code`").queue();
            return;
        }
        player.sendMessage(CoreUtils.translateAlternateColorCodes('&', Messages.LINK_LINKING.getMessage()));
        event.getHook().editOriginal("Linking to **" + player.getName() + "**... (`" + player.getUniqueId() + "`)").queue();
        ValorantBot.getInstance().getBot().removeLinkCode(code);
        ValorantData.getInstance().getData(player).setDiscordId(member.getIdLong());
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
        ValorantData.getInstance().getData(player).saveData();
        event.getHook().editOriginal("You are now linked to **" + player.getName() + "**! (`" + player.getUniqueId() + "`)").queue();
        player.sendMessage(CoreUtils.translateAlternateColorCodes('&',
                Messages.LINK_LINKED.getMessage(member.getUser().getName() + "#" + member.getUser().getDiscriminator())));
    }
}
