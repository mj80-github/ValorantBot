package dev.mj80.valorant.valorantbot.commands.impl.setup;

import dev.mj80.valorant.valorantbot.commands.DiscordCommand;
import dev.mj80.valorant.valorantbot.discord.Bot;
import dev.mj80.valorant.valorantbot.utils.BotUtils;
import dev.mj80.valorant.valorantbot.utils.CoreUtils;
import lombok.Getter;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.channel.ChannelType;
import net.dv8tion.jda.api.entities.channel.middleman.GuildChannel;
import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel;
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

        if ((member.isOwner()) || (BotUtils.checkRole(member, "admin"))) {
            if (BotUtils.checkChannel(event.getChannel(), "modCommand")) {
                if (CoreUtils.hasSetting(settings, "adminRole")) {
                    String setting = settings.stream().filter(line -> line.startsWith("adminRole")).findFirst().orElse("");
                    settings.set(settings.indexOf(setting), "adminRole = " + role);
                } else {
                    settings.add("adminRole = " + role);
                }
                CoreUtils.writeFileFromList("settings.txt", settings);

                BotUtils.auditAction("adminRole", event.getChannel(), member, event);

                event.getHook().editOriginal("Command was run successfully,").queue();
            } else {
                event.getHook().editOriginal("Sorry, you can't use this command in this channel.").queue();
            }
        } else {
            event.getHook().editOriginal("Sorry, you don't have permission to run this command.").queue();
        }
    }
}
