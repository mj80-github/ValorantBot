package dev.mj80.valorant.valorantbot.commands.impl.setup;

import dev.mj80.valorant.valorantbot.commands.DiscordCommand;
import dev.mj80.valorant.valorantbot.utils.BotUtils;
import dev.mj80.valorant.valorantbot.utils.CoreUtils;
import lombok.Getter;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
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
        Member member = event.getMember();
        long role = event.getOption("role").getAsLong();

        String settingsFile = CoreUtils.readFile("settings.txt");
        List<String> settings = new ArrayList<>(Arrays.asList(settingsFile.split("\n")));

        String adminRole;
        String setting = settings.stream().filter(line -> line.startsWith("adminRole")).findFirst().orElse("");

        if(!setting.equals("")) {
            adminRole = setting.substring(setting.indexOf("=") + 2);
        } else {
            adminRole = "0";
        }

        if (member.isOwner() || BotUtils.checkRole(member, Long.valueOf(adminRole))) {
            if (CoreUtils.hasSetting(settings, "adminRole")) {
                settings.set(settings.indexOf(setting), "adminRole = " + role);
            } else {
                settings.add("adminRole = " + role);
            }
            CoreUtils.writeFileFromList("settings.txt", settings);
        }

        event.getHook().deleteOriginal().queue();
    }
}
