package dev.mj80.valorant.valorantbot.commands.impl.help;

import dev.mj80.valorant.valorantbot.commands.DiscordCommand;
import dev.mj80.valorant.valorantbot.managers.CommandManager;
import dev.mj80.valorant.valorantbot.utils.BotUtils;
import dev.mj80.valorant.valorantbot.utils.CoreUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;
import net.dv8tion.jda.api.interactions.components.buttons.Button;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class Help extends DiscordCommand {
    public SlashCommandData getCommandData() {
        OptionData optionData = new OptionData(OptionType.STRING, "command", "The specific command", false);
        for(SlashCommandData slashCommandData : CommandManager.getCommandsData()) {
            String name = slashCommandData.getName();
            optionData.addChoice(name, name);
        }
        optionData.addChoice("help", "help");
        return Commands.slash("help", "Help menu")
                .addOptions(new OptionData(OptionType.STRING,"menu", "The menu of commands", false)
                                .addChoice("Setup", "setup")
                                .addChoice("Admin", "admin")
                                .addChoice("Mod", "mod")
                                .addChoice("Member", "member"),
                        optionData);
    }
    @Override
    public void run(SlashCommandInteractionEvent event) {
        event.deferReply().setEphemeral(true).queue();
        OptionMapping menuOption = event.getOption("menu");
        OptionMapping commandOption = event.getOption("command");
        String menu = null;
        String command = null;

        System.out.println(menuOption);
        System.out.println(commandOption);

        if (menuOption != null) {
            menu = menuOption.getAsString();
        }
        if (commandOption != null) {
            command = commandOption.getAsString();
        }

        EmbedBuilder embed = new EmbedBuilder();
        embed.setTitle("Help Menu", null);
        embed.setColor(Color.red);
        embed.setAuthor(Objects.requireNonNull(event.getJDA().getUserById(1053787916347908136L)).getName());
        embed.setFooter("Created by: " + Objects.requireNonNull(event.getJDA().getUserById(440183270328762388L)).getName());
        embed.setThumbnail(Objects.requireNonNull(event.getJDA().getUserById(1053787916347908136L)).getAvatarUrl());

        if (menu == null) {
            if (BotUtils.checkRole(event.getMember(), "admin")) {
                if (BotUtils.checkChannel(event.getChannel(), "botCommand")) {
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
                } else {
                    event.getHook().sendMessage("Sorry, you can't use this command in this channel.").queue();
                }
            } else if (BotUtils.checkRole(event.getMember(), "mod")) {
                if (BotUtils.checkChannel(event.getChannel(), "botCommand")) {
                    embed.addField("Main Menu", "This is the main menu of the /help command", false);
                    embed.addField("Usage", "Please use one of the buttons below \n Alternatively you can use \"/help <menu>\"", false);

                    embed.addBlankField(false);

                    embed.addField("Current Menus Supported", "\"Mod\",\"Member\"", false);

                    event.getHook().sendMessageEmbeds(embed.build())
                            .addActionRow(
                                    Button.primary("help-mod", "Mod"),
                                    Button.primary("help-member", "Member"))
                            .queue();
                } else {
                    event.getHook().sendMessage("Sorry, you can't use this command in this channel.").queue();
                }
            } else {
                if (BotUtils.checkChannel(event.getChannel(), "botCommand")) {
                    embed.addField("Main Menu", "This is the main menu of the /help command", false);
                    embed.addField("Usage", "Please use one of the buttons below \n Alternatively you can use \"/help <menu>\"", false);

                    embed.addBlankField(false);

                    embed.addField("Current Menus Supported", "\"Member\"", false);

                    event.getHook().sendMessageEmbeds(embed.build())
                            .addActionRow(
                                    Button.primary("help-member", "Member"))
                            .queue();
                } else {
                    event.getHook().sendMessage("Sorry, you can't use this command in this channel.").queue();
                }
            }
        } else if (menu.equals("setup")) {
            if (BotUtils.checkRole(event.getMember(), "admin")) {
                if (BotUtils.checkChannel(event.getChannel(), "botCommand")) {
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
                } else {
                    event.getHook().sendMessage("Sorry, you can't use this command in this channel.").queue();
                }
            } else {
                event.getHook().sendMessage("Sorry, you don't have permission to use this command.").queue();
            }
        } else if (menu.equals("admin")) {
            if (BotUtils.checkRole(event.getMember(), "admin")) {
                if (BotUtils.checkChannel(event.getChannel(), "botCommand")) {
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
                } else {
                    event.getHook().sendMessage("Sorry, you can't use this command in this channel.").queue();
                }
            } else {
                event.getHook().sendMessage("Sorry, you don't have permission to use this command.").queue();
            }
        } else if (menu.equals("mod")) {
            if (BotUtils.checkRole(event.getMember(), "admin")) {
                if (BotUtils.checkChannel(event.getChannel(), "botCommand")) {
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
                } else {
                    event.getHook().sendMessage("Sorry, you can't use this command in this channel.").queue();
                }
            } else if (BotUtils.checkRole(event.getMember(), "mod")) {
                if (BotUtils.checkChannel(event.getChannel(), "botCommand")) {
                    embed.setTitle("Mod Commands List", null);

                    embed.addField("WIP", "WIP", false);

                    embed.addBlankField(false);

                    embed.addField("Reminder", "If you need any help on the usage of the following commands \n Please use \"/help Mod <command>\"", false);

                    event.getHook().sendMessageEmbeds(embed.build())
                            .addActionRow(
                                    Button.primary("help-menu", "Main Menu"),
                                    Button.primary("help-member", "Member"))
                            .queue();
                } else {
                    event.getHook().sendMessage("Sorry, you can't use this command in this channel.").queue();
                }
            } else {
                event.getHook().sendMessage("Sorry, you don't have permission to use this command.").queue();
            }
        } else if (menu.equals("member")) {
            if (BotUtils.checkRole(event.getMember(), "admin")) {
                if (BotUtils.checkChannel(event.getChannel(), "botCommand")) {
                    embed.setTitle("Mod Commands List", null);

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
                } else {
                    event.getHook().sendMessage("Sorry, you can't use this command in this channel.").queue();
                }
            } else if (BotUtils.checkRole(event.getMember(), "mod")) {
                if (BotUtils.checkChannel(event.getChannel(), "botCommand")) {
                    embed.setTitle("Mod Commands List", null);

                    embed.addField("WIP", "WIP", false);

                    embed.addBlankField(false);

                    embed.addField("Reminder", "If you need any help on the usage of the following commands \n Please use \"/help Member <command>\"", false);

                    event.getHook().sendMessageEmbeds(embed.build())
                            .addActionRow(
                                    Button.primary("help-menu", "Main Menu"),
                                    Button.primary("help-mod", "Mod"))
                            .queue();
                } else {
                    event.getHook().sendMessage("Sorry, you can't use this command in this channel.").queue();
                }
            } else {
                if (BotUtils.checkChannel(event.getChannel(), "botCommand")) {
                    embed.setTitle("Member Commands List", null);

                    embed.addField("WIP", "WIP", false);

                    embed.addBlankField(false);

                    embed.addField("Reminder", "If you need any help on the usage of the following commands \n Please use \"/help Member <command>\"", false);

                    event.getHook().sendMessageEmbeds(embed.build())
                            .addActionRow(
                                    Button.primary("help-menu", "Main Menu"))
                            .queue();
                } else {
                    event.getHook().sendMessage("Sorry, you can't use this command in this channel.").queue();
                }
            }
        }
    }
}
