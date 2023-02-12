package dev.mj80.valorant.valorantbot.commands;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;

public abstract class DiscordCommand {
    public abstract SlashCommandData getCommandData();
    public abstract void run(SlashCommandInteractionEvent event);
}
