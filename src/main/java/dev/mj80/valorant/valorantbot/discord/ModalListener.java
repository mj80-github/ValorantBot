package dev.mj80.valorant.valorantbot.discord;

import dev.mj80.valorant.valorantbot.ValorantBot;
import dev.mj80.valorant.valorantbot.utils.CoreUtils;
import dev.mj80.valorant.valorantdata.DataUtils;
import dev.mj80.valorant.valorantdata.ValorantData;
import dev.mj80.valorant.valorantdata.penalty.Penalty;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.UserSnowflake;
import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

public class ModalListener extends ListenerAdapter {

    @Override
    public void onModalInteraction(@NotNull ModalInteractionEvent event) {
        event.deferReply().setEphemeral(true).queue();
        String settingsFile = CoreUtils.readFile("settings.txt");
        List<String> settings = new ArrayList<>(Arrays.asList(settingsFile.split("\n")));

        EmbedBuilder embed = new EmbedBuilder();
        embed.setColor(Color.red);
        embed.setAuthor(Objects.requireNonNull(event.getJDA().getUserById(1053787916347908136L)).getName());
        embed.setFooter("Created by: " + Objects.requireNonNull(event.getJDA().getUserById(440183270328762388L)).getName());
        embed.setThumbnail(Objects.requireNonNull(event.getJDA().getUserById(1053787916347908136L)).getAvatarUrl());

        switch (event.getModalId()) {
            case "appeal-modal" -> {
                embed.setColor(Color.orange);

                String member = event.getMember().getUser().getName();
                String id = event.getValue("appeal-id").getAsString();
                if(Pattern.matches("^#?\\d+$", event.getValue("appeal-id").getAsString())) {
                    id = id.replaceAll("#", "");
                } else {
                    event.getHook().sendMessage("Invalid punishment ID. Your ID should only be number.").setEphemeral(true).queue();
                    break;
                }
                String finalId = id;
                Penalty penalty = ValorantData.getInstance().getPenaltyManager().getPenalties().stream().filter(penalties -> penalties.getPID() == Integer.parseInt(finalId))
                        .findFirst().orElse(null);
                if(penalty == null || !penalty.getPlayerName().equalsIgnoreCase(event.getValue("appeal-name").getAsString())) {
                    event.getHook().sendMessage("Your punishment ID does not exist or it does not match your username.").setEphemeral(true).queue();
                    break;
                }
                if(penalty.getPenaltyType().isInstant()) {
                    event.getHook().sendMessage("This penalty type cannot be appealed.").setEphemeral(true).queue();
                    break;
                }
                if(!penalty.isActive()) {
                    event.getHook().sendMessage("This penalty is expired.").setEphemeral(true).queue();
                    break;
                }
 
                if (CoreUtils.hasSetting(settings, "appealChannel")) {
                    String appealChannel = settings.stream().filter(line -> line.startsWith("appealChannel")).findFirst().orElse("");
                    String appealChannelId = appealChannel.substring(appealChannel.indexOf("=") + 2);

                    embed.setTitle("New Appeal", null);

                    embed.addField("ID", id, false);
                    if (event.getValue("appeal-reason").getAsString().isBlank()) {
                        embed.addField("Reasoning", "No reasoning provided.", false);
                    } else {
                        embed.addField("Reasoning", event.getValue("appeal-reason").getAsString(), false);
                    }
                    embed.addField("User ID", member, false);

                    event.getGuild().getTextChannelById(appealChannelId).sendMessageEmbeds(embed.build())
                            .addActionRow(
                                    Button.primary("appeal-accept", "Accept"),
                                    Button.danger("appeal-decline", "Decline")
                            )
                            .queue();
                }

                event.getHook().sendMessage("Appeal has been successfully submitted.").setEphemeral(true).queue();
            }
            case ("ban-member-modal") -> {
                Member member = ValorantBot.getInstance().getBot().getGuild().getMembersByName(event.getValue("ban-member-member").getAsString(), false).stream().findFirst().orElse(null);
                Integer duration = Integer.parseInt(event.getValue("ban-member-duration").getAsString());
                String reason = event.getValue("ban-member-reason").getAsString();

                embed.setColor(Color.red);
                embed.setTitle("You Have Been Banned");
                embed.setFooter("Banned By: " + event.getMember().getUser().getName());

                embed.addField("Duration", DataUtils.timeLength(duration * 1000), false);
                embed.addField("Reasoning", reason, false);

                member.getUser().openPrivateChannel()
                        .flatMap(channel -> channel.sendMessageEmbeds(embed.build()))
                        .mapToResult()
                        .flatMap(result -> event.getGuild().ban(member, 1, TimeUnit.SECONDS))
                        .queue();

                //event.getGuild().ban(UserSnowflake.fromId(member.getIdLong()), 1, TimeUnit.SECONDS).queueAfter(5, TimeUnit.SECONDS);
                event.getGuild().unban(UserSnowflake.fromId(member.getIdLong())).queueAfter(duration, TimeUnit.SECONDS);

                event.getHook().editOriginal("Member has been banned successfully").queue();
            }
        }
    }
}
