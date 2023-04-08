package dev.mj80.valorant.valorantbot.commands.impl.setup;

import dev.mj80.valorant.valorantbot.utils.CoreUtils;
import dev.mj80.valorant.valorantbot.commands.DiscordCommand;
import lombok.Getter;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AdminRole extends DiscordCommand {

    @Getter
    private final SlashCommandData commandData =
            Commands.slash("adminrole", "Used to declare the admin role")
                    .addOption(OptionType.ROLE, "role", "The admin role", true);

    @Override
    public void run(SlashCommandInteractionEvent event) {
        event.deferReply().setEphemeral(true).queue();

        Long role = event.getOption("role").getAsLong();

        String settingsFile = CoreUtils.readFile("settings.txt");
        List<String> settings = new ArrayList<>(Arrays.asList(settingsFile.split("\n")));

        if (CoreUtils.hasRoleSetting(settings, "admin")) {
            Integer lineNum = 0;
            for (String line: settings) {
                if (line.startsWith("adminRole")) {
                    String oldSetting = line.substring(line.indexOf("=") + 2);
                    break;
                }
                lineNum++;
            }
            settings.set(lineNum, "adminRole = " + role.toString());
        } else {
            settings.add("adminRole = " + role.toString());
        }

        CoreUtils.writeFileFromList("setting.txt", settings);
    }
}
