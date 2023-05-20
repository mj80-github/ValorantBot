package dev.mj80.valorant.valorantbot.commands.impl.admin;

import dev.mj80.valorant.valorantbot.commands.DiscordCommand;
import dev.mj80.valorant.valorantbot.utils.CoreUtils;
import lombok.Getter;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class ListSettings extends DiscordCommand {

    @Getter
    private final SlashCommandData commandData =
            Commands.slash("listsettings", "Lists all current settings.");

    @Override
    public void run(SlashCommandInteractionEvent event) {
        event.deferReply().setEphemeral(true).queue();

        EmbedBuilder embed = new EmbedBuilder();
        embed.setTitle("Settings", null);
        embed.setColor(Color.red);
        embed.setAuthor(Objects.requireNonNull(event.getJDA().getUserById(1053787916347908136L)).getAsTag());
        embed.setFooter("Created by: " + Objects.requireNonNull(event.getJDA().getUserById(440183270328762388L)).getAsTag());
        embed.setThumbnail(Objects.requireNonNull(event.getJDA().getUserById(1053787916347908136L)).getAvatarUrl());

        StringBuilder stringBuilder = new StringBuilder();

        String settingsFile = CoreUtils.readFile("settings.txt");
        List<String> settings = new ArrayList<>(Arrays.asList(settingsFile.split("\n")));

        for (String setting: settings) {
            if (!Objects.equals(setting, "Settings")) {
                String name = setting.substring(0, setting.indexOf("=") + 2);
                Boolean checkForRole = setting.contains("Role");
                String value = setting.substring(setting.indexOf("=") + 2);

                if (checkForRole) {
                    stringBuilder.append(name).append(event.getJDA().getRoleById(value).getAsMention()).append("\n");
                } else {
                    stringBuilder.append(name).append(event.getJDA().getTextChannelById(value).getAsMention()).append("\n");
                }
            }
        }

        embed.addField("Settings List", String.valueOf(stringBuilder), false);
        event.getHook().sendMessageEmbeds(embed.build()).queue();
    }
}
