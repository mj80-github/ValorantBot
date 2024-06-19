package dev.cognitivity.valorant.valorantbot.discord;

import dev.cognitivity.valorant.valorantbot.ValorantBot;
import dev.cognitivity.valorant.valorantbot.utils.BotUtils;
import dev.cognitivity.valorant.valorantdata.DataUtils;
import dev.cognitivity.valorant.valorantdata.ValorantData;
import dev.cognitivity.valorant.valorantdata.data.StatData;
import dev.cognitivity.valorant.valorantdata.penalty.Penalty;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.dv8tion.jda.api.interactions.components.buttons.ButtonStyle;
import net.dv8tion.jda.api.interactions.components.text.TextInput;
import net.dv8tion.jda.api.interactions.components.text.TextInputStyle;
import net.dv8tion.jda.api.interactions.modals.Modal;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

public class ButtonListener extends ListenerAdapter {

    @Override
    public void onButtonInteraction(@NotNull ButtonInteractionEvent event) {
        EmbedBuilder embed = new EmbedBuilder();
        embed.setColor(Color.red);
        embed.setAuthor(Objects.requireNonNull(event.getJDA().getUserById(1053787916347908136L)).getName());
        embed.setFooter("Created by: " + Objects.requireNonNull(event.getJDA().getUserById(402136726363176963L)).getName());
        embed.setThumbnail(Objects.requireNonNull(event.getJDA().getUserById(1053787916347908136L)).getAvatarUrl());

        switch (Objects.requireNonNull(event.getButton().getId())) {
            case "appeals.create" -> {
                TextInput idInput = TextInput.create("appeal-id", "Penalty ID", TextInputStyle.SHORT).build();
                TextInput nameInput = TextInput.create("appeal-name", "Username or UUID", TextInputStyle.SHORT).build();

                event.replyModal(Modal.create("appeal-modal", "Appeal")
                        .addActionRow(idInput).addActionRow(nameInput).build()).queue();
            }
            case "appeal.close" -> {
                event.deferReply(true).queue();
                Member member = event.getMember();
                assert member != null;
                TextChannel channel = event.getChannel().asTextChannel();
                if(channel.getName().startsWith("appeal-")) {
                    channel.delete().queue();
                }
            }
            case "appeal.accept" -> {
                Member member = event.getMember();
                assert member != null;
                Guild guild = ValorantBot.getInstance().getBot().getGuild();
                assert guild != null;
                if(!member.hasPermission(Permission.MODERATE_MEMBERS)) {
                    event.reply("You cannot manage this appeal.").setEphemeral(true).queue();
                    break;
                }
                TextChannel channel = event.getChannel().asTextChannel();
                if(channel.getName().startsWith("appeal-")) {
                    String id = channel.getName().replace("appeal-", "");
                    Penalty penalty = Penalty.of(id);
                    assert penalty != null;
                    if(penalty.isAppealed()) {
                        event.reply("This appeal has already been decided.").setEphemeral(true).queue();
                        return;
                    }
                    penalty.setAppealed(true);
                    penalty.remove(member.getUser().getName()+" (via appeal)");

                    EmbedBuilder eb = new EmbedBuilder();
                    eb.setColor(Color.green);
                    eb.setTitle("Appeal Accepted");
                    eb.addField("Penalty ID", "#"+id, false);
                    eb.addField("Player", "`"+penalty.getPlayerUUID()+"`", false);
                    eb.addField("Penalty Reason", penalty.getReason(), false);
                    eb.addField("Staff", "`"+penalty.getStaffUUID()+"`", false);
                    eb.addField("Closed by", "`"+penalty.getStaffUUID()+"`", false);
                    eb.addField("Penalty Start", new SimpleDateFormat("dd/MM/yyyy @ HH:mm:ss z").format(new Date(penalty.getStart())), false);

                    Objects.requireNonNull(guild.getTextChannelById(1120868442162085969L)).sendMessageEmbeds(eb.build()).queue();

                    eb = new EmbedBuilder();
                    eb.setColor(Color.green);
                    eb.setTitle("Appeal Accepted");
                    eb.setDescription("This appeal has been accepted by "+member.getAsMention()+" and the penalty has been removed from your account." +
                            "\nClick the close button to close this channel.");
                    event.replyEmbeds(eb.build()).addActionRow(Button.of(ButtonStyle.SECONDARY, "appeal.close", "Close")).queue();

                    channel.getMemberPermissionOverrides().forEach(override -> override.getManager().deny(Permission.MESSAGE_SEND).queue());

                    String extraMessage = "### Appeal accepted by "+member.getUser().getName()+" - "+
                            new SimpleDateFormat("dd/MM/yyyy @ HH:mm:ss z").format(new Date(System.currentTimeMillis()))+" ###";
                    try {
                        if(penalty.getExtra() == null) {
                            penalty.getClass().getDeclaredMethod("setExtra", String.class).invoke(penalty, extraMessage);
                        } else {
                            penalty.getClass().getDeclaredMethod("setExtra", String.class).invoke(penalty, penalty.getExtra()+"\n"+extraMessage);
                        }
                    } catch (Exception exception) {}
                }
            }
            case "appeal.deny" -> {
                Member member = event.getMember();
                assert member != null;
                Guild guild = ValorantBot.getInstance().getBot().getGuild();
                assert guild != null;
                if(!member.hasPermission(Permission.MODERATE_MEMBERS)) {
                    event.reply("You cannot manage this appeal.").setEphemeral(true).queue();
                    break;
                }
                TextChannel channel = event.getChannel().asTextChannel();
                if(channel.getName().startsWith("appeal-")) {
                    String id = channel.getName().replace("appeal-", "");
                    Penalty penalty = Penalty.of(id);
                    assert penalty != null;
                    if(penalty.isAppealed()) {
                        event.reply("This appeal has already been decided.").setEphemeral(true).queue();
                        return;
                    }
                    penalty.setAppealed(true);
                    EmbedBuilder eb = new EmbedBuilder();
                    eb.setColor(Color.red);
                    eb.setTitle("Appeal Denied");
                    eb.addField("Penalty ID", "#"+id, false);
                    eb.addField("Player", "`"+penalty.getPlayerUUID()+"`", false);
                    eb.addField("Penalty Reason", penalty.getReason(), false);
                    eb.addField("Staff", "`"+penalty.getStaffUUID()+"`", false);
                    eb.addField("Closed by", "`"+penalty.getStaffUUID()+"`", false);
                    eb.addField("Penalty Start", new SimpleDateFormat("dd/MM/yyyy @ HH:mm:ss z").format(new Date(penalty.getStart())), false);

                    Objects.requireNonNull(guild.getTextChannelById(1120868442162085969L)).sendMessageEmbeds(eb.build()).queue();

                    eb = new EmbedBuilder();
                    eb.setColor(Color.red);
                    eb.setTitle("Appeal Denied");
                    eb.setDescription("This appeal has been denied by "+member.getAsMention()+".\nClick the close button to close this channel.");
                    event.replyEmbeds(eb.build()).addActionRow(Button.of(ButtonStyle.SECONDARY, "appeal.close", "Close")).queue();

                    channel.getMemberPermissionOverrides().forEach(override -> override.getManager().deny(Permission.MESSAGE_SEND).queue());

                    String extraMessage = "### Appeal denied by "+member.getUser().getName()+" - "+
                            new SimpleDateFormat("dd/MM/yyyy @ HH:mm:ss z").format(new Date(System.currentTimeMillis()))+" ###";
                    try {
                        if(penalty.getExtra() == null) {
                            penalty.getClass().getDeclaredMethod("setExtra", String.class).invoke(penalty, extraMessage);
                        } else {
                            penalty.getClass().getDeclaredMethod("setExtra", String.class).invoke(penalty, penalty.getExtra()+"\n"+extraMessage);
                        }
                    } catch (Exception exception) {}
                }
            }
            case "help-menu" -> {
                event.deferReply().setEphemeral(true).queue();
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
            }
            case "help-setup" -> {
                event.deferReply().setEphemeral(true).queue();
                if (BotUtils.checkRole(event.getMember(), "admin")) {
                    if (BotUtils.checkChannel(event.getChannel(), "botCommand")) {
                        embed.setTitle("Setup Commands List", null);

                        embed.addField("/adminrole", "Used to save the id of the admin role.", false);
                        embed.addField("/appealchannel", "Used to save the id of the channel where the appeals will be sent.", false);
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
            }
            case "help-admin" -> {
                event.deferReply().setEphemeral(true).queue();
                if (BotUtils.checkRole(event.getMember(), "admin")) {
                    if (BotUtils.checkChannel(event.getChannel(), "botCommand")) {
                        embed.setTitle("Admin Commands List", null);

                        embed.addField("/listsettings", "Lists all of the ids of the saved channels/roles",false);

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
            }
            case "help-mod" -> {
                event.deferReply().setEphemeral(true).queue();
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
            }
            case "help-member" -> {
                event.deferReply().setEphemeral(true).queue();
                if (BotUtils.checkRole(event.getMember(), "admin")) {
                    if (BotUtils.checkChannel(event.getChannel(), "botCommand")) {
                        embed.setTitle("Mod Commands List", null);

                        embed.addField("/appeal", "Appeal a punishment you have received.", false);

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

                        embed.addField("/appeal", "Appeal a punishment you have received.", false);

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

                        embed.addField("/appeal", "Appeal a punishment you have received.", false);

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
            case "appeal-accept" -> {
                event.deferReply().queue();

                event.getChannel().retrieveMessageById(event.getMessageId()).queue(p -> {
                    String id = p.getEmbeds().get(0).getFields().get(0).getValue();
                    String user = p.getEmbeds().get(0).getFields().get(2).getValue();

                    embed.setFooter("Accepted by: " + event.getMember().getUser().getName());
                    embed.setTitle("Appeal Accepted");
                    embed.setColor(Color.green);

                    Penalty penalty = ValorantData.getInstance().getPenaltyManager().getPenalties().stream().filter(penalties -> penalties.getId().equals(id)).findFirst().orElse(null);

                    embed.addField("Original Penalty", "```yaml\nType: Type\nLength: Length\nReason: Reason\nID: " + id + "\n```", false);
                    /* Remove comment when bot finalized
                    embed.addField("Original Penalty", "```yaml\nType: " + penalty.getPenaltyType() + "\nLength: " + penalty.getDuration() + "\nReason: " + penalty.getReason() + "\nID: " + id + "\n```", false);

                    penalty.remove(event.getMember().getUser().getName());
                    */

                    Member member = ValorantBot.getInstance().getBot().getGuild().getMembersByName(user, false).stream().findFirst().orElse(null);
                    member.getUser().openPrivateChannel()
                            .flatMap(channel -> channel.sendMessageEmbeds(embed.build()))
                            .queue();

                    BotUtils.auditAction("Accepted an appeal", event.getChannel(), event.getMember());

                    event.getMessage().delete().queue();
                    event.getHook().editOriginalEmbeds(embed.build()).queue();
                });
            }
            case "appeal-decline" -> {
                event.deferReply().queue();
                event.getChannel().retrieveMessageById(event.getMessageId()).queue(p -> {
                    String id = p.getEmbeds().get(0).getFields().get(0).getValue();
                    String user = p.getEmbeds().get(0).getFields().get(2).getValue();

                    embed.setFooter("Declined by: " + event.getUser().getName());
                    embed.setTitle("Appeal Declined");
                    embed.setColor(Color.red);


                    embed.addField("Original Penalty", "```yaml\nType: Type\nLength: Length\nReason: Reason\nID: " + id + "\n```", false);
                    //Remove comment when bot finalized
                    //embed.addField("Original Penalty", "```yaml\nType: " + penalty.getPenaltyType() + "\nLength: " + penalty.getDuration() + "\nReason: " + penalty.getReason() + "\nID: " + id + "\n```", false);

                    Member member = ValorantBot.getInstance().getBot().getGuild().getMembersByName(user, false).stream().findFirst().orElse(null);
                    member.getUser().openPrivateChannel()
                            .flatMap(channel -> channel.sendMessageEmbeds(embed.build()))
                            .queue();

                    BotUtils.auditAction("Declined an appeal", event.getChannel(), event.getMember());

                    event.getMessage().delete().queue();
                    event.getHook().editOriginalEmbeds(embed.build()).queue();
                });
            }
        }
    }
}
