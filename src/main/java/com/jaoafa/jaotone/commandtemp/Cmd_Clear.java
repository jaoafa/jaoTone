package com.jaoafa.jaotone.commandtemp;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import com.jaoafa.jaotone.libtemp.ToneLib;
import com.jaoafa.jaotone.player.PlayerManager;

public class Cmd_Clear extends Command {
    @SuppressWarnings("unused")
    public Cmd_Clear() {
        this.name = "clear";
        this.help = "キューをクリアします。";
        this.arguments = "";
    }

    @Override
    protected void execute(CommandEvent event) {
        if (!ToneLib.isJoinedVoiceChannel(event.getGuild())) {
            ToneLib.replyError(event, "ボイスチャンネルに接続していません。");
            return;
        }

        PlayerManager.getINSTANCE().getGuildMusicManager(event.getGuild()).player.stopTrack();
        PlayerManager.getINSTANCE().getGuildMusicManager(event.getGuild()).scheduler.getQueue().clear();
        event.reactSuccess();
    }
}
