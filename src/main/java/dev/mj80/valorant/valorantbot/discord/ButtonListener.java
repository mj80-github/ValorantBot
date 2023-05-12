package dev.mj80.valorant.valorantbot.discord;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.dv8tion.jda.api.utils.messages.MessageEditData;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.Objects;

public class ButtonListener extends ListenerAdapter {

    @Override
    public void onButtonInteraction(@NotNull ButtonInteractionEvent event) {
        EmbedBuilder embed = new EmbedBuilder();
        embed.setColor(Color.red);
        embed.setAuthor(Objects.requireNonNull(event.getJDA().getUserById(1053787916347908136L)).getAsTag());
        embed.setFooter("Created by: " + Objects.requireNonNull(event.getJDA().getUserById(440183270328762388L)).getAsTag());
        embed.setThumbnail(Objects.requireNonNull(event.getJDA().getUserById(1053787916347908136L)).getAvatarUrl());

        switch (event.getButton().getId()) {
            case "help-menu" -> {
                event.deferReply().setEphemeral(true).queue();
                embed.setTitle("Help Menu", null);

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
            case "help-setup" -> {
                event.deferReply().setEphemeral(true).queue();
                embed.setTitle("Setup Commands List", null);

                embed.addField("/adminrole", "Used to save the id of the admin role.", false);
                embed.addField("/auditchannel", "Used to save the id of the channel where command audits will be sent.", false);
                embed.addField("/commandchannel", "Used to save the id of the channel where the member commands must be used.", false);
                embed.addField("/modcommandchannel", "Used to save the id of the channel where the mod+ commands must be used.", false);
                embed.addField("/modrole", "Used to save the id of the mod role.", false);

                embed.addBlankField(false);

                embed.addField("Reminder", "If you need any help on the usage of the following commands \n Please use \"/help Setup <command>\"", false);

                event.getHook().sendMessageEmbeds(embed.build())
                        .addActionRow(
                                Button.primary("help-menu", "Main Menu"),
                                Button.primary("help-admin", "Admin"),
                                Button.primary("help-mod", "Mod"),
                                Button.primary("help-member", "Member"))
                        .queue();
            }
            case "help-admin" -> {
                event.deferReply().setEphemeral(true).queue();
                embed.setTitle("Admin Commands List", null);

                embed.addField("WIP", "WIP", false);

                embed.addBlankField(false);

                embed.addField("Reminder", "If you need any help on the usage of the following commands \n Please use \"/help Admin <command>\"", false);

                event.getHook().sendMessageEmbeds(embed.build())
                        .addActionRow(
                                Button.primary("help-menu", "Main Menu"),
                                Button.primary("help-setup", "Setup"),
                                Button.primary("help-mod", "Mod"),
                                Button.primary("help-member", "Member"))
                        .queue();
            }
            case "help-mod" -> {
                event.deferReply().setEphemeral(true).queue();
                embed.setTitle("Mod Commands List", null);

                embed.addField("WIP", "WIP", false);

                embed.addBlankField(false);

                embed.addField("Reminder", "If you need any help on the usage of the following commands \n Please use \"/help Mod <command>\"", false);

                event.getHook().sendMessageEmbeds(embed.build())
                        .addActionRow(
                                Button.primary("help-menu", "Main Menu"),
                                Button.primary("help-setup", "Setup"),
                                Button.primary("help-admin", "Admin"),
                                Button.primary("help-member", "Member"))
                        .queue();
            }
            case "help-member" -> {
                event.deferReply().setEphemeral(true).queue();
                embed.setTitle("Member Commands List", null);

                embed.addField("WIP", "WIP", false);

                embed.addBlankField(false);

                embed.addField("Reminder", "If you need any help on the usage of the following commands \n Please use \"/help Member <command>\"", false);

                event.getHook().sendMessageEmbeds(embed.build())
                        .addActionRow(
                                Button.primary("help-menu", "Main Menu"),
                                Button.primary("help-setup", "Setup"),
                                Button.primary("help-admin", "Admin"),
                                Button.primary("help-mod", "Mod"))
                        .queue();
            }
        }
    }
}
