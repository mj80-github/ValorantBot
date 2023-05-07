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

public class ModCommandChannel extends DiscordCommand {

    @Getter
    private final SlashCommandData commandData =
            Commands.slash("modcommandchannel", "Used to declare the channel where the mod commands should be used")
                    .addOption(OptionType.CHANNEL, "channel", "The mod command channel", true);

    @Override
    public void run(SlashCommandInteractionEvent event) {
        event.deferReply().setEphemeral(true).queue();
        long channel = event.getOption("channel").getAsLong();

        String settingsFile = CoreUtils.readFile("settings.txt");
        List<String> settings = new ArrayList<>(Arrays.asList(settingsFile.split("\n")));

        if (BotUtils.checkRole(event.getMember(), "admin")) {
            if (BotUtils.checkChannel(event.getChannel(), "modCommand")) {
                if (CoreUtils.hasSetting(settings, "modCommandChannel")) {
                    String setting = settings.stream().filter(line -> line.startsWith("modCommandChannel")).findFirst().orElse("");
                    settings.set(settings.indexOf(setting), "modCommandChannel = " + channel);
                } else {
                    settings.add("modCommandChannel = " + channel);
                }
                CoreUtils.writeFileFromList("settings.txt", settings);

                BotUtils.auditSlashCommandAction("modcommandchannel", event.getChannel(), event.getMember());

                event.getHook().editOriginal("Command was run successfully,").queue();
            } else {
                event.getHook().editOriginal("Sorry, you can't use this command in this channel.").queue();
            }
        } else {
            event.getHook().editOriginal("Sorry, you don't have permission to run this command.").queue();
        }
    }
}
