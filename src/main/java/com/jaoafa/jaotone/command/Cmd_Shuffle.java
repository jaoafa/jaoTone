package com.jaoafa.jaotone.command;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import com.jaoafa.jaotone.lib.ToneLib;
import com.jaoafa.jaotone.player.PlayerManager;

/**
 * コマンド: shuffle
 * <p>
 * キューをシャッフルします。コマンド実行時点で再生中の曲には影響しません。
 */
public class Cmd_Shuffle extends Command {
    /**
     * {@link Cmd_Shuffle} クラスの新しいインスタンスを初期化します。
     */
    @SuppressWarnings("unused")
    public Cmd_Shuffle() {
        this.name = "shuffle";
        this.help = "キューをシャッフルします。";
        this.arguments = "";
        this.aliases = new String[]{"sh", "resh"};
    }

    @Override
    protected void execute(CommandEvent event) {
        if (!ToneLib.isJoinedVoiceChannel(event.getGuild())) {
            ToneLib.replyError(event, "ボイスチャンネルに接続していません。");
            return;
        }

        PlayerManager.getINSTANCE().getGuildMusicManager(event.getGuild()).scheduler.shuffle();
        event.reactSuccess();
    }
}
