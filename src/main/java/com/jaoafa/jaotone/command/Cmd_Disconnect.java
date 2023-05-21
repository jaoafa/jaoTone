package com.jaoafa.jaotone.command;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import com.jaoafa.jaotone.lib.ToneLib;
import com.jaoafa.jaotone.player.PlayerManager;

/**
 * コマンド: disconnect
 * <p>
 * Bot をボイスチャンネルから切断します。
 */
public class Cmd_Disconnect extends Command {
    /**
     * {@link Cmd_Disconnect} クラスの新しいインスタンスを初期化します。
     */
    @SuppressWarnings("unused")
    public Cmd_Disconnect() {
        this.name = "disconnect";
        this.help = "ボイスチャンネルから切断します。";
        this.arguments = "";
        this.aliases = new String[]{"leave", "bye"};
    }

    @Override
    protected void execute(CommandEvent event) {
        if (!ToneLib.isJoinedVoiceChannel(event.getGuild())) {
            ToneLib.replyError(event, "ボイスチャンネルに接続していません。");
            return;
        }

        PlayerManager.destroyGuildMusicManager(event.getGuild());
        event.getGuild().getAudioManager().closeAudioConnection();
        event.reactSuccess();
    }
}