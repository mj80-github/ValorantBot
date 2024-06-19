package dev.cognitivity.valorant.valorantbot.commands;

import dev.cognitivity.valorant.valorantbot.ValorantBot;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;
import org.bukkit.scheduler.BukkitScheduler;

public abstract class DiscordCommand {
    public ValorantBot instance = ValorantBot.getInstance();
    public BukkitScheduler scheduler = instance.getServer().getScheduler();
    
    public abstract SlashCommandData getCommandData();
    public abstract void run(SlashCommandInteractionEvent event);
}
