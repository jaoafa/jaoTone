package com.jaoafa.jaotone.Lib.Discord;

import com.jaoafa.jaotone.Framework.Command.CmdEventContainer;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;

import javax.annotation.Nonnull;
import java.util.Arrays;

public class LibReply {
    public static void replyEmbeds(@Nonnull CmdEventContainer events, @Nonnull MessageEmbed... embeds) {
        switch (events.type()) {
            case App -> events.appEvent().replyEmbeds(Arrays.asList(embeds)).queue();
            case Text -> events.textEvent().getMessage().replyEmbeds(Arrays.asList(embeds)).queue();
        }
    }

    public static void reply(@Nonnull CmdEventContainer events, @Nonnull String content) {
        switch (events.type()) {
            case App -> events.appEvent().reply(content).queue();
            case Text -> events.textEvent().getMessage().reply(content).queue();
        }
    }

    public static void reply(@Nonnull CmdEventContainer events, @Nonnull Message message) {
        switch (events.type()) {
            case App -> events.appEvent().reply(message).queue();
            case Text -> events.textEvent().getMessage().reply(message).queue();
        }
    }
}
