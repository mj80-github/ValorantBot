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

public class ModRole extends DiscordCommand {

    @Getter
    private final SlashCommandData commandData =
            Commands.slash("modrole", "Used to declare the mod role")
                    .addOption(OptionType.ROLE, "role", "The mod role", true);

    @Override
    public void run(SlashCommandInteractionEvent event) {
        event.deferReply().setEphemeral(true).queue();
        long role = event.getOption("role").getAsLong();

        String settingsFile = CoreUtils.readFile("settings.txt");
        List<String> settings = new ArrayList<>(Arrays.asList(settingsFile.split("\n")));

        if (BotUtils.checkRole(event.getMember(), "admin")) {
            if (BotUtils.checkChannel(event.getChannel(), "modCommand")) {
                if (CoreUtils.hasSetting(settings, "modRole")) {
                    String setting = settings.stream().filter(line -> line.startsWith("modRole")).findFirst().orElse("");
                    settings.set(settings.indexOf(setting), "modRole = " + role);
                } else {
                    settings.add("modRole = " + role);
                }
                CoreUtils.writeFileFromList("settings.txt", settings);

                BotUtils.auditAction("Updated Mod Role", event.getChannel(), event.getMember());

                event.getHook().editOriginal("Command was run successfully,").queue();
            } else {
                event.getHook().editOriginal("Sorry, you can't use this command in this channel.").queue();
            }
        } else {
            event.getHook().editOriginal("Sorry, you don't have permission to run this command.").queue();
        }
    }
}
