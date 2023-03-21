package dev.mj80.valorant.valorantbot.commands.impl;

import dev.mj80.valorant.valorantbot.ValorantBot;
import dev.mj80.valorant.valorantbot.commands.DiscordCommand;
import dev.mj80.valorant.valorantdata.ValorantData;
import dev.mj80.valorant.valorantdata.data.StatData;
import lombok.Getter;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;
import org.bukkit.OfflinePlayer;

import java.util.UUID;
import java.util.regex.Pattern;

public class Stats extends DiscordCommand {
    @Getter
    private final SlashCommandData commandData =
            Commands.slash("stats", "Looks up a player's stats.")
                    .setGuildOnly(true)
                    .addOption(OptionType.STRING, "player", "A username or UUID.");
    
    
    @Override
    public void run(SlashCommandInteractionEvent event) {
        event.deferReply().queue();
        String input = event.getOption("player", OptionMapping::getAsString);
        OfflinePlayer player;
        if(input == null) {
            event.getHook().editOriginal("**ERROR** `NO NAME PROVIDED`").queue();
            return;
        }
        if(Pattern.matches("[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}", input)) {
            // UUID (e.g. 20210975-26f1-470c-b7e4-c52e3bed2222)
            player = ValorantBot.getInstance().getServer().getOfflinePlayer(UUID.fromString(input));
        } else if(Pattern.matches("[0-9a-f]{32}", input)) {
            // short UUID (e.g. 2021097526f1470cb7e4c52e3bed2222)
            StringBuilder stringBuilder = new StringBuilder(input).insert(8, "-").insert(13, "-").insert(18, "-").insert(23, "-");
            player = ValorantBot.getInstance().getServer().getOfflinePlayer(UUID.fromString(stringBuilder.toString()));
        } else if(Pattern.matches("[0-9A-Z_a-z]{3,16}", input)) {
            // USERNAME (e.g. mj80)
            player = ValorantBot.getInstance().getServer().getOfflinePlayer(input);
        } else {
            // illegal formatting (e.g. putting special characters in username, extra characters in uuid, etc.)
            event.getHook().editOriginal("**ERROR** `ILLEGAL FORMATTING`").queue();
            return;
        }
        if(!player.hasPlayedBefore()) {
            event.getHook().editOriginal("**ERROR** `UNKNOWN PLAYER`").queue();
            return;
        }
        event.getHook().editOriginal("**Fetching player** `"+player.getName()+"`**'s stats...**").queue();
        StatData stats = ValorantData.getInstance().getData(player).getStats();
        /*
         TODO get stats and send in embed
          - ADR
          - KD
          - Win%
          - total wins
          - total kills
          - total deaths
          - total assists
          - damage delta ((damageDealt - damageReceived) / roundsPlayed)
         */
    }
}
