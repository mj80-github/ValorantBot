package dev.cognitivity.valorant.valorantbot.utils;

import dev.cognitivity.valorant.valorantbot.ValorantBot;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.channel.Channel;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

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

    public static void auditAction(String action, Channel channel, Member member) {
        String settingsFile = CoreUtils.readFile("settings.txt");
        List<String> settings = new ArrayList<>(Arrays.asList(settingsFile.split("\n")));

        if (CoreUtils.hasSetting(settings, "auditLogChannel")) {
            String auditChannel = settings.stream().filter(line -> line.startsWith("auditLogChannel")).findFirst().orElse("");
            String auditChannelId = auditChannel.substring(auditChannel.indexOf("=") + 2);

            EmbedBuilder embed = new EmbedBuilder();
            embed.setTitle("Action Audited", null);
            embed.setColor(Color.red);
            embed.setAuthor(Objects.requireNonNull(ValorantBot.getInstance().getBot().getJda().getUserById(1053787916347908136L)).getName());
            embed.setFooter("Created by: " + Objects.requireNonNull(ValorantBot.getInstance().getBot().getJda().getUserById(440183270328762388L)).getName());
            embed.setThumbnail(Objects.requireNonNull(ValorantBot.getInstance().getBot().getJda().getUserById(1053787916347908136L)).getAvatarUrl());

            embed.addField("The following was completed:", "Action: " + action + "\nCompleted By: " + member.getUser().getAsMention() + "\nCompleted in: " + channel.getAsMention(), false);

            ValorantBot.getInstance().getBot().getJda().getTextChannelById(auditChannelId).sendMessageEmbeds(embed.build()).queue();
        }
    }
}
