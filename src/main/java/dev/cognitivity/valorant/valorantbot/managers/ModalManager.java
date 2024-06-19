package dev.cognitivity.valorant.valorantbot.managers;

import dev.cognitivity.valorant.valorantbot.modals.impl.AppealModal;
import dev.cognitivity.valorant.valorantbot.modals.DiscordModal;
import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class ModalManager extends ListenerAdapter {
    private final ArrayList<DiscordModal> modals = new ArrayList<>();

    public ModalManager() {
        modals.add(new AppealModal());
    }

    @Override
    public void onModalInteraction(@NotNull ModalInteractionEvent event) {
        modals.forEach(modal -> modal.onSubmit(event));
    }
}
