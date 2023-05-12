package dev.mj80.valorant.valorantbot.managers;

import dev.mj80.valorant.valorantbot.commands.DiscordCommand;
import dev.mj80.valorant.valorantbot.commands.impl.Link;
import dev.mj80.valorant.valorantbot.commands.impl.Stats;
import dev.mj80.valorant.valorantbot.commands.impl.help.Help;
import dev.mj80.valorant.valorantbot.commands.impl.setup.*;
import lombok.Getter;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;

import java.util.ArrayList;

public class CommandManager {
    @Getter ArrayList<DiscordCommand> commands = new ArrayList<>();
    
    public CommandManager() {
        commands.add(new Link());
        commands.add(new Stats());

        //Setup
        commands.add(new AdminRole());
        commands.add(new ModRole());
        commands.add(new AuditChannel());
        commands.add(new CommandChannel());
        commands.add(new ModCommandChannel());
        
        
        //Help (KEEP AT BOTTOM)
        commands.add(new Help());
    }
    public ArrayList<SlashCommandData> getCommandsData() {
        ArrayList<SlashCommandData> data = new ArrayList<>();
        for(DiscordCommand command : commands) {
            data.add(command.getCommandData());
        }
        return data;
    }
}
