package dev.mj80.valorant.valorantbot.commands.impl.setup;

import dev.mj80.valorant.valorantbot.CoreUtils;
import dev.mj80.valorant.valorantbot.commands.DiscordCommand;
import lombok.Getter;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;

import java.util.ArrayList;
import java.util.Arrays;

public class AdminRole extends DiscordCommand {
    @Getter
    private final SlashCommandData commandData =
            Commands.slash("adminrole", "Used to declare the admin role")
                    .addOption(OptionType.ROLE, "role", "The admin role");

    @Override
    public void run(SlashCommandInteractionEvent event) {
        event.deferReply().setEphemeral(true).queue();

        String settingsFile = CoreUtils.readFile("settings.txt");
        ArrayList<String> settings = new ArrayList<>(Arrays.asList(settingsFile.split("\n")));
        CoreUtils.writeFile("settings.txt", settingsFile+"\nlfmaofaoaomlflf");
        System.out.println(CoreUtils.readFile("settings.txt"));
    }
}
