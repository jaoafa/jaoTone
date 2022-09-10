package com.jaoafa.jaotone.command;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import com.jaoafa.jaotone.lib.ToneLib;
import com.jaoafa.jaotone.player.PlayerManager;

/**
 * コマンド: resume
 * <p>
 * 再生を再開します。{@link Cmd_Pause pause} コマンドで一時停止した場合に使用できます。
 *
 * @see Cmd_Pause
 */
public class Cmd_Resume extends Command {
    /**
     * {@link Cmd_Resume} クラスの新しいインスタンスを初期化します。
     */
    @SuppressWarnings("unused")
    public Cmd_Resume() {
        this.name = "resume";
        this.help = "再生を再開します。";
        this.arguments = "";
    }

    @Override
    protected void execute(CommandEvent event) {
        if (!ToneLib.isJoinedVoiceChannel(event.getGuild())) {
            ToneLib.replyError(event, "ボイスチャンネルに接続していません。");
            return;
        }

        PlayerManager.getINSTANCE().getGuildMusicManager(event.getGuild()).player.setPaused(false);
        event.reactSuccess();
    }
}
