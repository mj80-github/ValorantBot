package dev.cognitivity.valorant.valorantbot.discord;

import dev.cognitivity.valorant.valorantbot.ValorantBot;
import dev.cognitivity.valorant.valorantbot.managers.CommandManager;
import lombok.Getter;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.MemberCachePolicy;
import org.bukkit.entity.Player;

import java.util.*;

public class Bot {
    
    private final HashMap<Player, Short> linkCodes = new HashMap<>();
    @Getter
    private JDA jda;
    private final String token;
    public Bot(String token) {
        this.token = token;
    }
    public void build() {
        jda = JDABuilder.createDefault(token)
                .enableIntents(EnumSet.allOf(GatewayIntent.class))
                .setBulkDeleteSplittingEnabled(false)
                .setActivity(Activity.competing("VALORANT but Minecraft"))
                .setStatus(OnlineStatus.ONLINE)
                .setMemberCachePolicy(MemberCachePolicy.ALL)
                .addEventListeners(new MessageListener(), new ButtonListener(), ValorantBot.getInstance().getModalManager())
                .build();
        jda.updateCommands().addCommands(CommandManager.getCommandsData()).complete();
    }
    public short createLinkCode(Player player) {
        short code;
        if(!linkCodes.containsKey(player)) {
            code = (short) (new Random().nextInt(65536) - 32768);
            linkCodes.put(player, code);
        } else {
            code = linkCodes.get(player);
        }
        return code;
    }
    
    public void removeLinkCode(Short code) {
        linkCodes.remove(getLinkCode(code));
    }
    
    public Player getLinkCode(Short code) {
        for(Map.Entry<Player, Short> entry : linkCodes.entrySet()) {
            if(Objects.equals(entry.getValue(), code)) {
                return entry.getKey();
            }
        }
        return null;
    }
    
    public Short getLinkCode(Player player) {
        return linkCodes.get(player);
    }
    
    public void addRole(long userId, long roleId) {
        if(userId != 0) {
            Guild guild = getGuild();
            guild.addRoleToMember(Objects.requireNonNull(guild.getMemberById(userId)), Objects.requireNonNull(guild.getRoleById(roleId))).queue();
        }
    }
    
    public void removeRole(long userId, long roleId) {
        if(userId != 0) {
            Guild guild = getGuild();
            guild.removeRoleFromMember(Objects.requireNonNull(guild.getMemberById(userId)), Objects.requireNonNull(guild.getRoleById(roleId))).queue();
        }
    }
    
    public Member getMemberById(long userId) {
        Guild guild = getGuild();
        return guild.getMemberById(userId);
    }
    
    public Guild getGuild() {
        return jda.getGuildById(1053542951327895562L);
    }
}
