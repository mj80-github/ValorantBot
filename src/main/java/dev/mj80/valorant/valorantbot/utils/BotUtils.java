package dev.mj80.valorant.valorantbot.utils;

import dev.mj80.valorant.valorantbot.ValorantBot;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;

import java.util.List;

public class BotUtils {

    public static boolean checkRole(Member member, Long roleId) {
        List<Role> roles = ValorantBot.getInstance().getBot().getGuild().getRoles();
        List<Role> memberRoles = member.getRoles();

        Role targetRole = roles.stream().filter(role -> role.getId().equals(roleId.toString())).findFirst().orElse(null);
        return targetRole != null && memberRoles.contains(targetRole);
    }
}
