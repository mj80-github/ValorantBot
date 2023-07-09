package dev.mj80.valorant.valorantbot.commands.impl.mod;

import dev.mj80.valorant.valorantbot.ValorantBot;
import dev.mj80.valorant.valorantbot.commands.DiscordCommand;
import dev.mj80.valorant.valorantdata.ValorantData;
import dev.mj80.valorant.valorantdata.data.StatData;
import dev.mj80.valorant.valorantdata.penalty.Penalty;
import lombok.Getter;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;
import org.bukkit.OfflinePlayer;

import java.awt.*;
import java.util.ArrayList;
import java.util.Objects;

public class History extends DiscordCommand {

    @Getter
    private final SlashCommandData commandData =
            Commands.slash("history", "Lists the punishment history of the specified player.")
                    .addOption(OptionType.STRING, "Player-IGN", "The IGN of the player you would like to view the history for", true);

    @Override
    public void run(SlashCommandInteractionEvent event) {
        event.deferReply().setEphemeral(true).queue();

        String ign = event.getOption("Player-IGN").getAsString();

        EmbedBuilder embed = new EmbedBuilder();
        embed.setTitle("Penalties for " + ign, null);
        embed.setColor(Color.red);
        embed.setAuthor(Objects.requireNonNull(event.getJDA().getUserById(1053787916347908136L)).getName());
        embed.setFooter("Created by: " + Objects.requireNonNull(event.getJDA().getUserById(440183270328762388L)).getName());

        StringBuilder stringBuilder = new StringBuilder();

        OfflinePlayer player = ValorantBot.getInstance().getServer().getOfflinePlayer(ign);
        StatData data = ValorantData.getInstance().getData(player).getStats();
        ArrayList<Penalty> penalties = data.getPenalties();

        for (Penalty penalty: penalties) {
            stringBuilder.append(penalty).append("\n");
        }
        embed.addField("Penalties", String.valueOf(stringBuilder), false);
        event.replyEmbeds(embed.build());
    }
}
