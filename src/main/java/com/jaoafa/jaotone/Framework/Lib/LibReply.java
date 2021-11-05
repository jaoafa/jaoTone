package com.jaoafa.jaotone.Framework.Lib;

import com.jaoafa.jaotone.Framework.Command.CmdEventContainer;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;

import javax.annotation.Nonnull;
import java.util.Arrays;

public class LibReply {
    public static LibReplyAction replyEmbeds(@Nonnull CmdEventContainer events, @Nonnull MessageEmbed... embeds) {
        return switch (events.type()) {
            case App -> {
                SlashCommandEvent appEvent = events.appEvent();
                if (appEvent.isAcknowledged())
                    yield new LibReplyAction(appEvent.getChannel().sendMessageEmbeds(Arrays.asList(embeds)));

                yield new LibReplyAction(appEvent.replyEmbeds(Arrays.asList(embeds)));
            }
            case Text -> new LibReplyAction(events.textEvent().getMessage().replyEmbeds(Arrays.asList(embeds)));
        };
    }

    public static LibReplyAction reply(@Nonnull CmdEventContainer events, @Nonnull String content) {
        return switch (events.type()) {
            case App -> {
                SlashCommandEvent appEvent = events.appEvent();
                if (appEvent.isAcknowledged())
                    yield new LibReplyAction(appEvent.getChannel().sendMessage(content));

                yield new LibReplyAction(appEvent.reply(content));
            }
            case Text -> new LibReplyAction(events.textEvent().getMessage().reply(content));
        };
    }

    public static LibReplyAction reply(@Nonnull CmdEventContainer events, @Nonnull Message message) {
        return switch (events.type()) {
            case App -> {
                SlashCommandEvent appEvent = events.appEvent();
                if (appEvent.isAcknowledged())
                    yield new LibReplyAction(appEvent.getChannel().sendMessage(message));

                yield new LibReplyAction(events.appEvent().reply(message));
            }
            case Text -> new LibReplyAction(events.textEvent().getMessage().reply(message));
        };
    }
}
