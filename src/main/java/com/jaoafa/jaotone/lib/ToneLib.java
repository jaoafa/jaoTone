package com.jaoafa.jaotone.lib;

import com.jagrosh.jdautilities.command.CommandEvent;
import net.dv8tion.jda.api.entities.Guild;

/**
 * jaoTone のライブラリクラスです。
 */
public class ToneLib {
    /**
     * {@link CommandEvent} のメッセージに対してメンションを送信します。
     * <p>
     * {@link CommandEvent#reply(String)} とは以下の点で異なります。
     * <ul>
     *     <li>送信メッセージの参照先が設定されます</li>
     *     <li>当該ユーザーにメンションが送信されます</li>
     *     <li>2000 文字以上のメッセージを送信するとエラーが発生します（split されません）</li>
     *     <li>参照先メッセージが削除されてもメッセージが削除されません</li>
     * </ul>
     *
     * @param event {@link CommandEvent}
     * @param text  メッセージコンテンツ
     */
    public static void reply(CommandEvent event, String text) {
        event.getMessage().reply(text).queue();
    }

    /**
     * {@link CommandEvent} のメッセージに対して「失敗」としてメンションを送信します。
     *
     * @param event {@link CommandEvent}
     * @param text  メッセージコンテンツ
     * @see ToneLib#reply(CommandEvent, String)
     */
    public static void replyError(CommandEvent event, String text) {
        reply(event, ":x: " + text);
        event.reactError();
    }

    /**
     * Bot 自身が指定された {@link Guild} でボイスチャンネルに参加しているかどうかを返します。
     *
     * @param guild {@link Guild}
     * @return ボイスチャンネルに参加しているかどうか
     */
    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    public static boolean isJoinedVoiceChannel(Guild guild) {
        return guild.getAudioManager().isConnected();
    }

    /**
     * 秒数のみで表現された時間表記を Human readable な時間表記に変換します。
     *
     * @param time 秒数
     * @return Human readable な時間表記
     */
    public static String formatTime(long time) {
        long seconds = time / 1000;
        long minutes = seconds / 60;
        long hours = minutes / 60;
        long days = hours / 24;
        seconds %= 60;
        minutes %= 60;
        hours %= 24;
        return (days > 0 ? days + "日" : "") + (hours > 0 ? hours + "時間" : "") + (minutes > 0 ?
                minutes + "分" :
                "") + seconds + "秒";
    }
}
