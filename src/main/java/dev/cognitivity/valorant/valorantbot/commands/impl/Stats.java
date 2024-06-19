package dev.cognitivity.valorant.valorantbot.commands.impl;

import dev.cognitivity.valorant.valorantbot.commands.DiscordCommand;
import dev.cognitivity.valorant.valorantdata.DataUtils;
import dev.cognitivity.valorant.valorantdata.ValorantData;
import dev.cognitivity.valorant.valorantdata.data.StatData;
import lombok.Getter;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;
import org.bukkit.OfflinePlayer;

import java.awt.*;
import java.util.Objects;
import java.util.UUID;
import java.util.regex.Pattern;

public class Stats extends DiscordCommand {
    @Getter
    private final SlashCommandData commandData =
            Commands.slash("stats", "Looks up a player's stats.")
                    .setGuildOnly(true)
                    .addOption(OptionType.STRING, "player", "A username or UUID.", true);
    
    
    @Override
    public void run(SlashCommandInteractionEvent event) {
        event.deferReply().setEphemeral(true).queue();
        String input = event.getOption("player").getAsString();
        // run asynchronously since getOfflinePlayer usually takes a few seconds
        scheduler.runTaskAsynchronously(instance, () -> {
            OfflinePlayer player;
            if(Pattern.matches("[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}", input)) {
                // UUID (e.g. 20210975-26f1-470c-b7e4-c52e3bed2222)
                player = instance.getServer().getOfflinePlayer(UUID.fromString(input));
            } else if(Pattern.matches("[0-9a-f]{32}", input)) {
                // short UUID (e.g. 2021097526f1470cb7e4c52e3bed2222)
                StringBuilder stringBuilder = new StringBuilder(input).insert(8, "-").insert(13, "-").insert(18, "-").insert(23, "-");
                player = instance.getServer().getOfflinePlayer(UUID.fromString(stringBuilder.toString()));
            } else if(Pattern.matches("[0-9A-Z_a-z]{3,16}", input)) {
                // USERNAME (e.g. cognitivity)
                player = instance.getServer().getOfflinePlayer(input);
            } else {
                // illegal formatting (e.g. putting special characters in username, extra characters in uuid, etc.)
                event.getHook().editOriginal("**ERROR** `ILLEGAL FORMATTING`").queue();
                return;
            }
            if(!player.hasPlayedBefore()) {
                event.getHook().editOriginal("**ERROR** `UNKNOWN PLAYER`").queue();
                return;
            }

            StatData stats = ValorantData.getInstance().getData(player).getStats();

            EmbedBuilder embed = new EmbedBuilder();
            embed.setTitle("Stats for " + player.getName(), null);
            embed.setColor(Color.red);
            embed.setAuthor(Objects.requireNonNull(event.getJDA().getUserById(1053787916347908136L)).getName());
            embed.setFooter("Created by: " + Objects.requireNonNull(event.getJDA().getUserById(440183270328762388L)).getName());
            embed.setThumbnail(Objects.requireNonNull(event.getJDA().getUserById(1053787916347908136L)).getAvatarUrl());

            embed.addField("Stats: ", "```yaml" +
                    "\nKDR: " + DataUtils.round((float)stats.getKills()/stats.getDeaths(), 2) +
                    "\nADR: " + DataUtils.round((float)stats.getDamageDealt()/stats.getRoundsPlayed(), 2) +
                    "\nTotal Wins: " + stats.getVictories() +
                    "\nWins %: " + DataUtils.round((float)stats.getVictories()/stats.getMatchesPlayed(), 2) * 100 +
                    "\nTotal Kills: " + stats.getKills() +
                    "\nTotal Deaths: " + stats.getDeaths() +
                    "\nTotal Assists: " + stats.getAssists() +
                    "\nDamage Delta: " + DataUtils.round((float)(stats.getDamageDealt() - stats.getDamageReceived())/stats.getRoundsPlayed(), 2) +
                    "\n```", false);

            event.getHook().sendMessageEmbeds(embed.build()).queue();
        });
    }
}
