package dev.cognitivity.valorant.valorantbot.modals;

import dev.cognitivity.valorant.valorantdata.ValorantData;
import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent;
import org.jetbrains.annotations.NotNull;

public abstract class DiscordModal {
    private final @NotNull String id;
    public DiscordModal(@NotNull String id) {
        this.id = id;
    }
    protected abstract void submit(ModalInteractionEvent event);

    public void onSubmit(ModalInteractionEvent event) {
        if(event.getModalId().equals(id)) {
            ValorantData.getInstance().log("Submitting modal with id \""+id+"\" for user "+event.getUser().getName());
            event.deferReply(true).queue();
            submit(event);
        }
    }
}
