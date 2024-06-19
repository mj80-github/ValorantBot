package dev.cognitivity.valorant.valorantbot.commands.impl.admin;

import dev.cognitivity.valorant.valorantbot.commands.DiscordCommand;
import dev.cognitivity.valorant.valorantbot.utils.BotUtils;
import dev.cognitivity.valorant.valorantbot.utils.CoreUtils;
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
        embed.setAuthor(Objects.requireNonNull(event.getJDA().getUserById(1053787916347908136L)).getName());
        embed.setFooter("Created by: " + Objects.requireNonNull(event.getJDA().getUserById(440183270328762388L)).getName());
        embed.setThumbnail(Objects.requireNonNull(event.getJDA().getUserById(1053787916347908136L)).getAvatarUrl());

        StringBuilder stringBuilder = new StringBuilder();

        String settingsFile = CoreUtils.readFile("settings.txt");
        List<String> settings = new ArrayList<>(Arrays.asList(settingsFile.split("\n")));

        if (BotUtils.checkRole(event.getMember(), "admin")) {
            if (BotUtils.checkChannel(event.getChannel(), "modCommand")) {
                for (String setting : settings) {
                    if (!Objects.equals(setting, "Settings")) {
                        String name = setting.substring(0, setting.indexOf("=") + 2);
                        boolean checkForRole = setting.contains("Role");
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
            } else {
                event.getHook().editOriginal("Sorry, you can't use this command in this channel.").queue();
            }
        } else {
            event.getHook().editOriginal("Sorry, you don't have permission to run this command.").queue();
        }
    }
}
