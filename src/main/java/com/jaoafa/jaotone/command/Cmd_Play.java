package com.jaoafa.jaotone.command;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import com.jaoafa.jaotone.lib.ToneLib;
import com.jaoafa.jaotone.player.PlayerManager;

import java.nio.file.Files;
import java.nio.file.Path;

/**
 * コマンド: play
 * <p>
 * 指定された URL の音楽を再生します。または指定されたキーワードで検索し、最初にヒットした曲を再生します。
 */
public class Cmd_Play extends Command {
    /**
     * {@link Cmd_Play} クラスの新しいインスタンスを初期化します。
     */
    @SuppressWarnings("unused")
    public Cmd_Play() {
        this.name = "play";
        this.help = "音楽を再生します。";
        this.arguments = "<LinkOrQuery>";
        this.aliases = new String[]{"p", "add"};
    }

    @Override
    protected void execute(CommandEvent event) {
        // クエリはURLか検索ワードのいずれかである
        String query = event.getArgs();
        if (query.isEmpty()) {
            ToneLib.replyError(event, "クエリが指定されていません。");
            return;
        }

        if (!ToneLib.isJoinedVoiceChannel(event.getGuild())) {
            if (event.getMember().getVoiceState() == null || !event.getMember().getVoiceState().inAudioChannel()) {
                ToneLib.replyError(event, "ボイスチャンネルに参加していません。");
                return;
            }
            event.getGuild().getAudioManager().openAudioConnection(event.getMember().getVoiceState().getChannel());
        }


        // クエリがURLかパスかどうかを判定する
        //noinspection HttpUrlsUsage
        if (query.startsWith("http://") || query.startsWith("https://")) {
            // URLの場合
            PlayerManager.loadAndPlay(event, event.getArgs());
            return;
        } else if (Files.exists(Path.of(query))) {
            // パスの場合
            PlayerManager.loadAndPlay(event, query);
            return;
        }

        // 検索ワードの場合
        String searchQuery = String.format("ytsearch:%s", query);
        PlayerManager.loadAndPlay(event, searchQuery);
    }
}