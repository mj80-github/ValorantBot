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
import java.util.Objects;

public class AdminRole extends DiscordCommand {

    @Getter
    private final SlashCommandData commandData =
            Commands.slash("adminrole", "Used to declare the admin role")
                    .addOption(OptionType.ROLE, "role", "The admin role", true);

    @Override
    public void run(SlashCommandInteractionEvent event) {
        event.deferReply().setEphemeral(true).queue();

        
        if(event.getOption("role") == null) {
            event.getHook().editOriginal("**ERROR** `Not an integer`").queue();
            return;
        }
        long role = Objects.requireNonNull(event.getOption("role")).getAsLong();

        String settingsFile = CoreUtils.readFile("settings.txt");
        List<String> settings = new ArrayList<>(Arrays.asList(settingsFile.split("\n")));

        if (CoreUtils.hasRoleSetting(settings, "admin")) {
            settings.stream().filter(line -> line.startsWith("adminRole")).findFirst().ifPresent(setting -> {
                settings.set(settings.indexOf(setting), "adminRole = " + role);
            });
        } else {
            settings.add("adminRole = " + role);
        }

        CoreUtils.writeFileFromList("settings.txt", settings);
    }
}
