package dev.mj80.valorant.valorantbot.discord;

import dev.mj80.valorant.valorantbot.ValorantBot;
import dev.mj80.valorant.valorantbot.utils.CoreUtils;
import dev.mj80.valorant.valorantdata.ValorantData;
import dev.mj80.valorant.valorantdata.penalty.Penalty;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import org.checkerframework.checker.units.qual.Length;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class ModalListener extends ListenerAdapter {

    @Override
    public void onModalInteraction(@NotNull ModalInteractionEvent event) {
        event.deferReply().setEphemeral(true).queue();
        String settingsFile = CoreUtils.readFile("settings.txt");
        List<String> settings = new ArrayList<>(Arrays.asList(settingsFile.split("\n")));

        EmbedBuilder embed = new EmbedBuilder();
        embed.setColor(Color.red);
        embed.setAuthor(Objects.requireNonNull(event.getJDA().getUserById(1053787916347908136L)).getName());
        embed.setFooter("Created by: " + Objects.requireNonNull(event.getJDA().getUserById(440183270328762388L)).getName());
        embed.setThumbnail(Objects.requireNonNull(event.getJDA().getUserById(1053787916347908136L)).getAvatarUrl());

        switch (event.getModalId()) {
            case "appeal-modal" -> {
                String member = event.getMember().getUser().getName();

                if (CoreUtils.hasSetting(settings, "appealChannel")) {
                    String appealChannel = settings.stream().filter(line -> line.startsWith("appealChannel")).findFirst().orElse("");
                    String appealChannelId = appealChannel.substring(appealChannel.indexOf("=") + 2);

                    embed.setTitle("New Appeal", null);

                    embed.addField("ID", event.getValue("appeal-id").getAsString(), false);
                    if (event.getValue("appeal-reason").getAsString().isBlank()) {
                        embed.addField("Reasoning", "No reasoning provided.", false);
                    } else {
                        embed.addField("Reasoning", event.getValue("appeal-reason").getAsString(), false);
                    }
                    embed.addField("User ID", member, false);

                    event.getGuild().getTextChannelById(appealChannelId).sendMessageEmbeds(embed.build())
                            .addActionRow(
                                    Button.primary("appeal-accept", "Accept"),
                                    Button.danger("appeal-decline", "Decline")
                            )
                            .queue();
                }

                event.getHook().sendMessage("Appeal has been successfully submitted.").setEphemeral(true).queue();
            }
        }
    }
}
