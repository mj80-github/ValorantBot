package dev.mj80.valorant.valorantbot.commands.impl.setup;

import dev.mj80.valorant.valorantbot.commands.DiscordCommand;
import dev.mj80.valorant.valorantbot.utils.BotUtils;
import dev.mj80.valorant.valorantbot.utils.CoreUtils;
import lombok.Getter;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AppealChannel extends DiscordCommand {
    @Getter
    private final SlashCommandData commandData =
            Commands.slash("appealchannel", "Used to declare the channel where the appeals will be posted")
                    .addOption(OptionType.CHANNEL, "channel", "The appeal channel", true);
    @Override
    public void run(SlashCommandInteractionEvent event) {
        event.deferReply().setEphemeral(true).queue();
        long channel = event.getOption("channel").getAsLong();

        String settingsFile = CoreUtils.readFile("settings.txt");
        List<String> settings = new ArrayList<>(Arrays.asList(settingsFile.split("\n")));

        if (BotUtils.checkRole(event.getMember(), "admin")) {
            if (BotUtils.checkChannel(event.getChannel(), "modCommand")) {
                if (CoreUtils.hasSetting(settings, "appealChannel")) {
                    String setting = settings.stream().filter(line -> line.startsWith("appealChannel")).findFirst().orElse("");
                    settings.set(settings.indexOf(setting), "appealChannel = " + channel);
                } else {
                    settings.add("appealChannel = " + channel);
                }
                CoreUtils.writeFileFromList("settings.txt", settings);

                BotUtils.auditSlashCommandAction("appealchannel", event.getChannel(), event.getMember());

                event.getHook().editOriginal("Command was successfully run").queue();
            } else {
                event.getHook().editOriginal("Sorry, you can't use this command in this channel.").queue();
            }
        } else {
            event.getHook().editOriginal("Sorry, you don't have permission to run this command.").queue();
        }
    }
}
