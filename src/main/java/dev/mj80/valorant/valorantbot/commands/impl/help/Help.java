package dev.mj80.valorant.valorantbot.commands.impl.help;

import dev.mj80.valorant.valorantbot.commands.DiscordCommand;
import lombok.Getter;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;

import java.awt.*;

public class Help extends DiscordCommand {
    @Getter private final SlashCommandData commandData =
            Commands.slash("help", "Help menu");

    @Override
    public void run(SlashCommandInteractionEvent event) {

        event.deferReply().setEphemeral(true).queue();

        EmbedBuilder embed = new EmbedBuilder();

        embed.setTitle("Help Menu", null);

        embed.setColor(Color.red);

        embed.addField("Admin", "List of All Admin Commands (/help admin)", false);
        embed.addField("Moderator", "List of All Moderator Commands (/help mod)", false);
        embed.addField("Member", "List of All Member Commands (/help member)", false);
        embed.addBlankField(false);

        embed.setAuthor(event.getJDA().getUserById(1053787916347908136l).getAsTag());

        embed.setFooter("Created by: " + event.getJDA().getUserById(440183270328762388l).getAsTag());

        embed.setThumbnail(event.getJDA().getUserById(1053787916347908136l).getAvatarUrl());

        event.getHook().sendMessageEmbeds(embed.build()).queue();
    }
}
