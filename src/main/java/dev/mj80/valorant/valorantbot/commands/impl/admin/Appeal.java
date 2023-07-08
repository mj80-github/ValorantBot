package dev.mj80.valorant.valorantbot.commands.impl.admin;

import dev.mj80.valorant.valorantbot.commands.DiscordCommand;
import dev.mj80.valorant.valorantbot.utils.CoreUtils;
import lombok.Getter;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.text.TextInput;
import net.dv8tion.jda.api.interactions.components.text.TextInputStyle;
import net.dv8tion.jda.api.interactions.modals.Modal;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Appeal extends DiscordCommand {
    @Getter
    private final SlashCommandData commandData =
            Commands.slash("appeal", "Appeal a punishment that was given.");

    @Override
    public void run(SlashCommandInteractionEvent event) {

        TextInput id = TextInput.create("appeal-id", "Punishment ID", TextInputStyle.SHORT)
                .setRequired(true)
                .build();

        TextInput reason = TextInput.create("appeal-reason", "Reason (Optional)", TextInputStyle.PARAGRAPH)
                .setRequired(false)
                .setMaxLength(250)
                .build();

        Modal appealModal = Modal.create("appeal-modal", "Penalty Appeal")
                .addActionRows(ActionRow.of(id), ActionRow.of(reason))
                .build();

        event.replyModal(appealModal).queue();
    }
}
