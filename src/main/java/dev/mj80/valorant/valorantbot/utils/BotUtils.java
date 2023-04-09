package dev.mj80.valorant.valorantbot.utils;

import dev.mj80.valorant.valorantbot.ValorantBot;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;

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
            String setting = String.valueOf(settings.stream().filter(line -> line.startsWith(role + "Role")).findFirst());
            roleId = setting.substring(setting.indexOf("=") + 2);
        } else {
            roleId = "0";
        }

        Role targetRole = roles.stream().filter(currentRole -> currentRole.getId().equals(roleId)).findFirst().orElse(null);
        return targetRole != null && memberRoles.contains(targetRole);
    }
}
