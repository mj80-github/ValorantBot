package dev.mj80.valorant.valorantbot.commands.impl.help;

import dev.mj80.valorant.valorantbot.commands.DiscordCommand;
import lombok.Getter;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.buttons.Button;

import java.awt.*;
import java.util.Objects;

public class Help extends DiscordCommand {
    @Getter private final SlashCommandData commandData =
            Commands.slash("help", "Help menu");

    @Override
    public void run(SlashCommandInteractionEvent event) {
        event.deferReply().setEphemeral(true).queue();

        EmbedBuilder embed = new EmbedBuilder();
        embed.setTitle("Help Menu", null);
        embed.setColor(Color.red);
        embed.setAuthor(Objects.requireNonNull(event.getJDA().getUserById(1053787916347908136L)).getAsTag());
        embed.setFooter("Created by: " + Objects.requireNonNull(event.getJDA().getUserById(440183270328762388L)).getAsTag());
        embed.setThumbnail(Objects.requireNonNull(event.getJDA().getUserById(1053787916347908136L)).getAvatarUrl());

        embed.addField("Main Menu", "This is the main menu of the /help command", false);
        embed.addField("Usage", "Please use one of the buttons below \n Alternatively you can use \"/help <menu>\"", false);

        embed.addBlankField(false);

        embed.addField("Current Menus Supported", "\"Setup\",\"Admin\",\"Mod\",\"Member\"", false);

        event.getHook().sendMessageEmbeds(embed.build())
                .addActionRow(
                        Button.primary("help-setup", "Setup"),
                        Button.primary("help-admin", "Admin"),
                        Button.primary("help-mod", "Mod"),
                        Button.primary("help-member", "Member"))
                .queue();
    }
}
