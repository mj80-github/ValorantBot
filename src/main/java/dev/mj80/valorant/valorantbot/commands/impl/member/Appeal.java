package dev.mj80.valorant.valorantbot.commands.impl.member;

import dev.mj80.valorant.valorantbot.commands.DiscordCommand;
import lombok.Getter;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.text.TextInput;
import net.dv8tion.jda.api.interactions.components.text.TextInputStyle;
import net.dv8tion.jda.api.interactions.modals.Modal;

public class Appeal extends DiscordCommand {
    @Getter
    private final SlashCommandData commandData =
            Commands.slash("appeal", "Appeal a punishment that was given.");

    @Override
    public void run(SlashCommandInteractionEvent event) {
        
        TextInput username = TextInput.create("appeal-name", "Minecraft Username", TextInputStyle.SHORT)
                .setRequired(true)
                .setMinLength(3)
                .setMaxLength(16)
                .build();
        
        TextInput id = TextInput.create("appeal-id", "Punishment ID", TextInputStyle.SHORT)
                .setRequired(true)
                .build();

        TextInput reason = TextInput.create("appeal-reason", "Reason (Optional)", TextInputStyle.PARAGRAPH)
                .setRequired(false)
                .setMaxLength(512)
                .build();

        Modal appealModal = Modal.create("appeal-modal", "Penalty Appeal")
                .addComponents(ActionRow.of(username), ActionRow.of(id), ActionRow.of(reason))
                .build();

        event.replyModal(appealModal).queue();
    }
}
