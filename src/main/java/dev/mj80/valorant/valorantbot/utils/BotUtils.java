package dev.mj80.valorant.valorantbot.utils;

import dev.mj80.valorant.valorantbot.ValorantBot;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.channel.Channel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.InteractionHook;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BotUtils {

    public static boolean checkRole(Member member, String role) {
        String settingsFile = CoreUtils.readFile("settings.txt");
        List<String> settings = new ArrayList<>(Arrays.asList(settingsFile.split("\n")));
        List<Role> roles = ValorantBot.getInstance().getBot().getGuild().getRoles();
        List<Role> memberRoles = member.getRoles();
        String roleId;

        if (CoreUtils.hasSetting(settings, role + "Role")) {
            String setting = settings.stream().filter(line -> line.startsWith(role + "Role")).findFirst().orElse("");
            roleId = setting.substring(setting.indexOf("=") + 2);
        } else {
            System.out.println("does not have setting");
            roleId = "0";
        }

        Role targetRole = roles.stream().filter(currentRole -> currentRole.getId().equals(roleId)).findFirst().orElse(null);
        return targetRole != null && memberRoles.contains(targetRole);
    }

    public static boolean checkChannel(Channel channel, String correctChannel) {
        String settingsFile = CoreUtils.readFile("settings.txt");
        List<String> settings = new ArrayList<>(Arrays.asList(settingsFile.split("\n")));
        String channelId = channel.getId();
        String correctChannelId;

        if (CoreUtils.hasSetting(settings, correctChannel + "Channel")) {
            String setting = settings.stream().filter(line -> line.startsWith(correctChannel + "Channel")).findFirst().orElse("");
            correctChannelId = setting.substring(setting.indexOf("=") + 2);
        } else {
            correctChannelId = "0";
        }

        return channelId.equals(correctChannelId);
    }

    public static void auditSlashCommandAction(String slashCommand, Channel channel, Member member) {
        String settingsFile = CoreUtils.readFile("settings.txt");
        List<String> settings = new ArrayList<>(Arrays.asList(settingsFile.split("\n")));

        if (CoreUtils.hasSetting(settings, "auditLogChannel")) {
            String auditChannel = settings.stream().filter(line -> line.startsWith("auditLogChannel")).findFirst().orElse("");
            String auditChannelId = auditChannel.substring(auditChannel.indexOf("=") + 2);

            ValorantBot.getInstance().getBot().getJda().getTextChannelById(auditChannelId).sendMessage("User " + member.getUser().getAsTag() + " used the command " + slashCommand + " in " + channel.getAsMention()).queue();
        }
    }
}
