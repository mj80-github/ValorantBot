package dev.cognitivity.valorant.valorantbot.commands.impl.setup;

import dev.cognitivity.valorant.valorantbot.commands.DiscordCommand;
import dev.cognitivity.valorant.valorantbot.utils.BotUtils;
import dev.cognitivity.valorant.valorantbot.utils.CoreUtils;
import lombok.Getter;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AuditChannel extends DiscordCommand {

    @Getter
    private final SlashCommandData commandData =
            Commands.slash("auditchannel", "Used to declare the channel where the audit log will be posted")
                    .addOption(OptionType.CHANNEL, "channel", "The audit log channel", true);

    @Override
    public void run(SlashCommandInteractionEvent event) {
        event.deferReply().setEphemeral(true).queue();
        long channel = event.getOption("channel").getAsLong();

        String settingsFile = CoreUtils.readFile("settings.txt");
        List<String> settings = new ArrayList<>(Arrays.asList(settingsFile.split("\n")));

        if (BotUtils.checkRole(event.getMember(), "admin")) {
            if (BotUtils.checkChannel(event.getChannel(), "modCommand")) {
                if (CoreUtils.hasSetting(settings, "auditLogChannel")) {
                    String setting = settings.stream().filter(line -> line.startsWith("auditLogChannel")).findFirst().orElse("");
                    settings.set(settings.indexOf(setting), "auditLogChannel = " + channel);
                } else {
                    settings.add("auditLogChannel = " + channel);
                }
                CoreUtils.writeFileFromList("settings.txt", settings);

                BotUtils.auditAction("Updated Audit Channel", event.getChannel(), event.getMember());

                event.getHook().editOriginal("Command was successfully run").queue();
            } else {
                event.getHook().editOriginal("Sorry, you can't use this command in this channel.").queue();
            }
        } else {
            event.getHook().editOriginal("Sorry, you don't have permission to run this command.").queue();
        }
    }
}
