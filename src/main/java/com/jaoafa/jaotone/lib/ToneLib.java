package com.jaoafa.jaotone.lib;

import com.jagrosh.jdautilities.command.CommandEvent;
import net.dv8tion.jda.api.entities.Guild;

public class ToneLib {
    public static void reply(CommandEvent event, String text) {
        event.getMessage().reply(text).queue();
    }

    public static void replySuccess(CommandEvent event, String text) {
        reply(event, ":white_check_mark: " + text);
    }

    public static void replyError(CommandEvent event, String text) {
        reply(event, ":x: " + text);
        event.reactError();
    }

    public static boolean isJoinedVoiceChannel(Guild guild) {
        return guild.getAudioManager().isConnected();
    }

    public static String formatTime(long time) {
        long seconds = time / 1000;
        long minutes = seconds / 60;
        long hours = minutes / 60;
        long days = hours / 24;
        seconds %= 60;
        minutes %= 60;
        hours %= 24;
        return (days > 0 ? days + "日" : "") + (hours > 0 ? hours + ":" : "") + (minutes > 0 ? minutes + "分" : "") + seconds + "秒";
    }
}
