package dev.cognitivity.valorant.valorantbot.modals.impl;

import dev.cognitivity.valorant.valorantbot.ValorantBot;
import dev.cognitivity.valorant.valorantbot.modals.DiscordModal;
import dev.cognitivity.valorant.valorantdata.DataUtils;
import dev.cognitivity.valorant.valorantdata.ValorantData;
import dev.cognitivity.valorant.valorantdata.data.StatData;
import dev.cognitivity.valorant.valorantdata.penalty.Penalty;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.dv8tion.jda.api.interactions.components.buttons.ButtonStyle;
import org.bukkit.OfflinePlayer;

import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.regex.Pattern;

public class AppealModal extends DiscordModal {
    public AppealModal() {
        super("appeal-modal");
    }

    @Override
    protected void submit(ModalInteractionEvent event) {
        Member member = event.getMember();
        assert member != null;
        String id = Objects.requireNonNull(event.getValue("appeal-id")).getAsString();
        if(Pattern.matches("^#?[A-Fa-f0-9]+$", id)) {
            id = id.replaceAll("#", "").toUpperCase();
        } else {
            event.getHook().sendMessage("Invalid punishment ID. Your ID should be 8 letters or numbers.").setEphemeral(true).queue();
            return;
        }
        String finalId = id;
        Penalty penalty = ValorantData.getInstance().getPenaltyManager().getPenalties().stream().filter(penalties -> penalties.getId().equals(finalId))
                .findFirst().orElse(null);
        String name = Objects.requireNonNull(event.getValue("appeal-name")).getAsString();
        OfflinePlayer player;
        if(Pattern.matches("^[A-z\\d_]{3,16}$", name)) { // USERNAME
            player = ValorantBot.getInstance().getServer().getOfflinePlayer(name);
        } else if(Pattern.matches("^[\\da-f]{8}-([\\da-f]{4}-){3}[\\da-f]{12}$", name)) { // LONG UUID
            player = ValorantBot.getInstance().getServer().getOfflinePlayer(UUID.fromString(name));
        } else if(Pattern.matches("^[\\da-f]{32}$", name)) { // TRIMMED UUID
            StringBuilder uuid = new StringBuilder(name).insert(8, "-").insert(13, "-").insert(18, "-").insert(23, "-");
            player = ValorantBot.getInstance().getServer().getOfflinePlayer(UUID.fromString(uuid.toString()));
        } else {
            player = null;
        }
        if(player == null) {
            event.getHook().editOriginal("Incorrect format for username.").queue();
            return;
        }
        if(!player.hasPlayedBefore()) {
            event.getHook().editOriginal("Invalid player. Is this the right account?").queue();
            return;
        }
        if(penalty == null || !penalty.getPlayerUUID().equals(player.getUniqueId())) {
            event.getHook().editOriginal("Incorrect ID or username.\nIf you have recently changed your username, use your Minecraft UUID instead.").queue();
            return;
        }
        if(penalty.getPenaltyType().isInstant()) {
            event.getHook().editOriginal("This penalty type cannot be appealed.").queue();
            return;
        }
        if(!penalty.isActive()) {
            event.getHook().editOriginal("This penalty is expired.").queue();
            return;
        }
        if(penalty.isAppealed()) {
            event.getHook().editOriginal("This penalty has already been appealed.").queue();
            return;
        }
        Guild guild = ValorantBot.getInstance().getBot().getGuild();
        java.util.List<TextChannel> channels = guild.getTextChannelsByName("appeal-"+id.toLowerCase(), false);
        if(!channels.isEmpty()) {
            event.getHook().editOriginal("This appeal already exists: "+channels.get(0).getAsMention()).queue();
            return;
        }
        event.getHook().editOriginal("Creating appeal...").queue();
        guild.createTextChannel("appeal-"+id, guild.getCategoryById(1232068286049685667L)).syncPermissionOverrides().addMemberPermissionOverride(member.getIdLong(), java.util.List.of(
                Permission.MESSAGE_SEND,
                Permission.VIEW_CHANNEL
        ), List.of()).queue((channel) -> {
            SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy @ HH:mm:ss z");

            EmbedBuilder infoEmbed = new EmbedBuilder();
            infoEmbed.setColor(Color.red);
            infoEmbed.setTitle("Appeal #"+finalId);
            infoEmbed.addField("Player Name", penalty.getPlayerName(), false);
            infoEmbed.addField("Staff Name", penalty.getStaffName(), false);

            infoEmbed.addField("Player UUID", "`"+penalty.getPlayerUUID().toString()+"`", false);
            infoEmbed.addField("Staff UUID", "`"+penalty.getStaffUUID().toString()+"`", false);

            StatData playerData = ValorantData.getInstance().getData(player).getStats();
            StatData staffData = ValorantData.getInstance().getData(ValorantBot.getInstance().getServer().getOfflinePlayer(penalty.getStaffUUID())).getStats();
            infoEmbed.addField("Player Discord", playerData.getDiscordId() == 0 ? "Unlinked ("+member.getIdLong()+")"
                    : playerData.getDiscordId()+" ("+member.getIdLong()+")", false);
            infoEmbed.addField("Staff Discord", staffData.getDiscordId() == 0 ? "Unlinked" : String.valueOf(staffData.getDiscordId()), false);

            infoEmbed.addField("ID", finalId, false);
            infoEmbed.addField("Duration", DataUtils.timeLength(penalty.getDuration()), false);

            infoEmbed.addField("Start", format.format(new Date(penalty.getStart())), false);
            infoEmbed.addField("End", format.format(new Date(penalty.getStart())), false);

            infoEmbed.addField("Reason", penalty.getReason(), false);

            EmbedBuilder extraEmbed = new EmbedBuilder();
            extraEmbed.setColor(Color.darkGray);
            extraEmbed.setTitle("Extra Information");
            if(penalty.getExtra() == null) {
                extraEmbed.setDescription("```inform7\nNo information provided.\n```");
            } else {
                extraEmbed.setDescription("```inform7\n"+penalty.getExtra()+"\n```");
                extraEmbed.setFooter("This information was provided by the issuing staff member at the time of the penalty. Chat logs are unmodified.");
            }
            extraEmbed.setAuthor(penalty.getStaffName());

            channel.sendMessageEmbeds(infoEmbed.build()).addActionRow(Button.of(ButtonStyle.SUCCESS, "appeal.accept", "Accept"),
                    Button.of(ButtonStyle.DANGER, "appeal.deny", "Deny")).queue();
            channel.sendMessageEmbeds(extraEmbed.build()).queue();

            Member staffMember = guild.getMemberById(staffData.getDiscordId());
            channel.sendMessage(member.getAsMention() + (staffMember != null ? " "+staffMember.getAsMention() : "")).queue(message -> message.delete().queue());

            event.getHook().editOriginal("Appeal created. "+channel.getAsMention()).queue();
        }, (throwable) -> event.getHook().editOriginal("An error occurred.").queue());
    }
}
