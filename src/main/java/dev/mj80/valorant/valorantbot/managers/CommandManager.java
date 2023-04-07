package dev.mj80.valorant.valorantbot.managers;

import dev.mj80.valorant.valorantbot.commands.DiscordCommand;
import dev.mj80.valorant.valorantbot.commands.impl.Link;
import dev.mj80.valorant.valorantbot.commands.impl.Stats;
import dev.mj80.valorant.valorantbot.commands.impl.help.Help;
import dev.mj80.valorant.valorantbot.commands.impl.setup.AdminRole;
import lombok.Getter;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;

import java.util.ArrayList;

public class CommandManager {
    @Getter ArrayList<DiscordCommand> commands = new ArrayList<>();
    @Getter ArrayList<SlashCommandData> commandsData = new ArrayList<>();
    
    public CommandManager() {
        commands.add(new Link());
        commands.add(new Stats());
        commands.add(new Help());
        commands.add(new AdminRole());
        
        commands.forEach(command -> commandsData.add(command.getCommandData()));
    }
}
