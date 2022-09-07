package com.jaoafa.jaotone.lib;

import com.jagrosh.jdautilities.command.CommandEvent;

public class ToneLib {
    public static void reply(CommandEvent event, String text) {
        event.getMessage().reply(text).queue();
    }

    public static void replySuccess(CommandEvent event, String text) {
        reply(event, ":white_check_mark: " + text);
    }

    public static void replyError(CommandEvent event, String text) {
        reply(event, ":x: " + text);
    }

    public static void replyWarning(CommandEvent event, String text) {
        reply(event, ":warning: " + text);
    }
}
