package com.jaoafa.jaotone.event;

import com.jaoafa.jaotone.player.PlayerManager;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.utils.messages.MessageCreateData;
import org.jetbrains.annotations.NotNull;

public class Event_QueueButton extends ListenerAdapter {
    @Override
    public void onButtonInteraction(@NotNull ButtonInteractionEvent event) {
        if (!event.getComponentId().startsWith("queue:")) {
            return;
        }

        Message message = event.getMessage();
        Message refMessage = message.getReferencedMessage();
        message.delete().queue();

        String[] args = event.getComponentId().split(":");
        int page = Integer.parseInt(args[1]);

        MessageCreateData messageCreateData = PlayerManager.getINSTANCE().getQueueEmbed(event.getGuild(), page);
        if (messageCreateData == null) {
            if (refMessage == null) {
                event.getChannel().sendMessage(":x: キューが空です。").queue();
                return;
            }
            refMessage.reply(":x: キューが空です。").queue();
            return;
        }

        if (refMessage == null) {
            event.getChannel().sendMessage(messageCreateData).queue();
            return;
        }
        event.getMessage().reply(messageCreateData).queue();
    }
}
