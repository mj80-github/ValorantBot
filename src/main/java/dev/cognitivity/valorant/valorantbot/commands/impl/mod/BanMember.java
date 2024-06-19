package dev.cognitivity.valorant.valorantbot.commands.impl.mod;

import dev.cognitivity.valorant.valorantbot.commands.DiscordCommand;
import lombok.Getter;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.text.TextInput;
import net.dv8tion.jda.api.interactions.components.text.TextInputStyle;
import net.dv8tion.jda.api.interactions.modals.Modal;

public class BanMember extends DiscordCommand {
    @Getter
    private final SlashCommandData commandData =
            Commands.slash("banmember", "Bans a specific member.");


    @Override
    public void run(SlashCommandInteractionEvent event) {
        TextInput member = TextInput.create("ban-member-member", "Member's Discord Name", TextInputStyle.SHORT)
                .setRequired(true)
                .build();

        TextInput duration = TextInput.create("ban-member-duration", "Duration (seconds)", TextInputStyle.SHORT)
                .setRequired(true)
                .build();

        TextInput reason = TextInput.create("ban-member-reason", "Reason", TextInputStyle.PARAGRAPH)
                .setRequired(false)
                .build();

        Modal banMemberModal = Modal.create("ban-member-modal", "Ban Member")
                .addComponents( ActionRow.of(member), ActionRow.of(duration), ActionRow.of(reason))
                .build();

        event.replyModal(banMemberModal).queue();
    }
}
