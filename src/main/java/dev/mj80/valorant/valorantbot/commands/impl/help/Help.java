package dev.mj80.valorant.valorantbot.commands.impl.help;

import dev.mj80.valorant.valorantbot.ValorantBot;
import dev.mj80.valorant.valorantbot.commands.DiscordCommand;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;
import net.dv8tion.jda.api.interactions.components.buttons.Button;

import java.awt.*;
import java.util.Objects;

public class Help extends DiscordCommand {
    public SlashCommandData getCommandData() {
        OptionData optionData = new OptionData(OptionType.STRING, "command", "The specific command", false);
        for(SlashCommandData slashCommandData : ValorantBot.getInstance().getCommandManager().getCommandsData()) {
            String name = slashCommandData.getName();
            optionData.addChoice(name, "Usage of /"+name+" command");
        }
        return Commands.slash("help", "Help menu")
                .addOptions(new OptionData(OptionType.STRING,"menu", "The menu of commands", false)
                                .addChoice("Setup", "List of all Setup commands")
                                .addChoice("Admin", "List of all Admin commands")
                                .addChoice("Mod", "List of all Mod Commands")
                                .addChoice("Member", "List of all Member Commands"),
                        optionData);
    }
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
