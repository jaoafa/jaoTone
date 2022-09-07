package com.jaoafa.jaotone.commandtemp;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import com.jaoafa.jaotone.libtemp.ToneLib;
import com.jaoafa.jaotone.player.PlayerManager;

public class Cmd_Resume extends Command {
    @SuppressWarnings("unused")
    public Cmd_Resume() {
        this.name = "resume";
        this.help = "一時停止中のトラックを再生します。";
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
