package dev.mj80.valorant.valorantbot.utils;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.User;

import java.util.List;

public class BotUtils {

    public static boolean checkRole(Member member, Long role, List<Role> roleList) {
        List<net.dv8tion.jda.api.entities.Role> memberRoles = member.getRoles();

        if (roleList.stream().filter(roles -> roles.getId().equals(role.toString())).findFirst().orElse(null) != null) {
            if (memberRoles.stream().filter(roles -> roles.getId().equals(role.toString())).findFirst().orElse(null) != null) {
                return true;
            }
        }
        return false;
    }
}
