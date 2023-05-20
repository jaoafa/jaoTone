package com.jaoafa.jaotone.command;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import com.jaoafa.jaotone.lib.ToneLib;
import com.jaoafa.jaotone.player.PlayerManager;

/**
 * コマンド: skip
 * <p>
 * 再生中のトラックをスキップします。
 */
public class Cmd_Skip extends Command {
    /**
     * {@link Cmd_Skip} クラスの新しいインスタンスを初期化します。
     */
    @SuppressWarnings("unused")
    public Cmd_Skip() {
        this.name = "skip";
        this.help = "再生中のトラックをスキップします。";
        this.arguments = "";
        this.aliases = new String[]{"s"};
    }

    @Override
    protected void execute(CommandEvent event) {
        if (!ToneLib.isJoinedVoiceChannel(event.getGuild())) {
            ToneLib.replyError(event, "ボイスチャンネルに接続していません。");
            return;
        }

        boolean result = PlayerManager.getGuildMusicManager(event.getGuild()).scheduler.nextTrack();
        if (!result) {
            event.reactError();
        } else {
            event.reactSuccess();
        }
    }
}