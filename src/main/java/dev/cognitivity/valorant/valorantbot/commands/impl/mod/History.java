package dev.cognitivity.valorant.valorantbot.commands.impl.mod;

import dev.cognitivity.valorant.valorantbot.ValorantBot;
import dev.cognitivity.valorant.valorantbot.commands.DiscordCommand;
import dev.cognitivity.valorant.valorantdata.DataUtils;
import dev.cognitivity.valorant.valorantdata.ValorantData;
import dev.cognitivity.valorant.valorantdata.data.StatData;
import dev.cognitivity.valorant.valorantdata.penalty.Penalty;
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
                    .addOption(OptionType.STRING, "player", "The IGN of the player you would like to view the history for", true)
                    .addOption(OptionType.STRING, "id", "The specific ID of a punishment for this player.");

    @Override
    public void run(SlashCommandInteractionEvent event) {
        event.deferReply().setEphemeral(true).queue();

        String ign = event.getOption("player").getAsString();

        EmbedBuilder embed = new EmbedBuilder();
        embed.setColor(Color.red);
        embed.setAuthor(Objects.requireNonNull(event.getJDA().getUserById(1053787916347908136L)).getName());

        StringBuilder stringBuilder = new StringBuilder();

        OfflinePlayer player = ValorantBot.getInstance().getServer().getOfflinePlayer(ign);
        if (!player.hasPlayedBefore()) {
            event.getHook().editOriginal("No player by that name found.").queue();
            return;
        }
        StatData data = ValorantData.getInstance().getData(player).getStats();
        ArrayList<Penalty> penalties = data.getPenalties();
        embed.setTitle("Penalties for " + penalties.get(0).getPlayerName(), null);

        try {
            String id = event.getOption("id").getAsString();
            Penalty penalty = Penalty.of(id);

            if (penalty.isActive()) {
                if (penalty.getDuration() <= 0) {
                    stringBuilder.append("```yaml\nID: ").append(penalty.getId()).append("\nType: ").append(penalty.getPenaltyType()).append("\nActive: ").append(penalty.isActive()).append("\nPenalized by: ").append(penalty.getStaffName()).append("\n```").append("\n");
                } else {
                    stringBuilder.append("```yaml\nID: ").append(penalty.getId()).append("\nType: ").append(penalty.getPenaltyType()).append("\nDuration: ").append(DataUtils.timeLength(penalty.getDuration())).append("\nActive: ").append(penalty.isActive()).append("\nTime Left: ").append(DataUtils.timeUntil(penalty.getEnd())).append("\nPenalized by: ").append(penalty.getStaffName()).append("\n```").append("\n");
                }
            } else {
                if (penalty.getDuration() <= 0) {
                    stringBuilder.append("```yaml\nID: ").append(penalty.getId()).append("\nType: ").append(penalty.getPenaltyType()).append("\nActive: ").append(penalty.isActive()).append("\nPenalized by: ").append(penalty.getStaffName()).append("\n```").append("\n");
                } else {
                    stringBuilder.append("```yaml\nID: ").append(penalty.getId()).append("\nType: ").append(penalty.getPenaltyType()).append("\nDuration: ").append(DataUtils.timeLength(penalty.getDuration())).append("\nActive: ").append(penalty.isActive()).append("\nPenalized by: ").append(penalty.getStaffName()).append("\n```").append("\n");
                }
            }
        } catch (Exception e) {
            for (Penalty penalty: penalties) {
                if (penalty.isActive()) {
                    if (penalty.getDuration() <= 0) {
                        stringBuilder.append("```yaml\nID: ").append(penalty.getId()).append("\nType: ").append(penalty.getPenaltyType()).append("\nActive: ").append(penalty.isActive()).append("\nPenalized by: ").append(penalty.getStaffName()).append("\n```").append("\n");
                    } else {
                        stringBuilder.append("```yaml\nID: ").append(penalty.getId()).append("\nType: ").append(penalty.getPenaltyType()).append("\nDuration: ").append(DataUtils.timeLength(penalty.getDuration())).append("\nActive: ").append(penalty.isActive()).append("\nTime Left: ").append(DataUtils.timeUntil(penalty.getEnd())).append("\nPenalized by: ").append(penalty.getStaffName()).append("\n```").append("\n");
                    }
                } else {
                    if (penalty.getDuration() <= 0) {
                        stringBuilder.append("```yaml\nID: ").append(penalty.getId()).append("\nType: ").append(penalty.getPenaltyType()).append("\nActive: ").append(penalty.isActive()).append("\nPenalized by: ").append(penalty.getStaffName()).append("\n```").append("\n");
                    } else {
                        stringBuilder.append("```yaml\nID: ").append(penalty.getId()).append("\nType: ").append(penalty.getPenaltyType()).append("\nDuration: ").append(DataUtils.timeLength(penalty.getDuration())).append("\nActive: ").append(penalty.isActive()).append("\nPenalized by: ").append(penalty.getStaffName()).append("\n```").append("\n");
                    }
                }
            }
        }

        embed.addField("Penalties:", String.valueOf(stringBuilder), false);
        event.getHook().sendMessageEmbeds(embed.build()).queue();
    }
}
