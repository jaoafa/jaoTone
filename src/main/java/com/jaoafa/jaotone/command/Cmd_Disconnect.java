package com.jaoafa.jaotone.command;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import com.jaoafa.jaotone.lib.ToneLib;
import com.jaoafa.jaotone.player.PlayerManager;

public class Cmd_Disconnect extends Command {
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

        PlayerManager.getINSTANCE().getGuildMusicManager(event.getGuild()).player.stopTrack();
        PlayerManager.getINSTANCE().getGuildMusicManager(event.getGuild()).scheduler.getQueue().clear();
        event.getGuild().getAudioManager().closeAudioConnection();
        event.reactSuccess();
    }
}