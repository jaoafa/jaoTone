package com.jaoafa.jaotone.command;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import com.jaoafa.jaotone.lib.ToneLib;
import com.jaoafa.jaotone.player.PlayerManager;

/**
 * コマンド: pause
 * <p>
 * 再生中のトラックを一時停止します。{@link Cmd_Resume resume} コマンドで再生を再開できます。
 *
 * @see Cmd_Resume
 */
public class Cmd_Pause extends Command {
    /**
     * {@link Cmd_Pause} クラスの新しいインスタンスを初期化します。
     */
    @SuppressWarnings("unused")
    public Cmd_Pause() {
        this.name = "pause";
        this.help = "再生中のトラックを一時停止します。";
        this.arguments = "";
    }

    @Override
    protected void execute(CommandEvent event) {
        if (!ToneLib.isJoinedVoiceChannel(event.getGuild())) {
            ToneLib.replyError(event, "ボイスチャンネルに接続していません。");
            return;
        }

        PlayerManager.getGuildMusicManager(event.getGuild()).player.setPaused(true);
        event.reactSuccess();
    }
}
