package com.jaoafa.jaotone.commandtemp;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import com.jaoafa.jaotone.libtemp.ToneLib;
import com.jaoafa.jaotone.player.PlayerManager;

public class Cmd_Skip extends Command {
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

        boolean result = PlayerManager.getINSTANCE().getGuildMusicManager(event.getGuild()).scheduler.nextTrack();
        if (!result) {
            event.reactError();
        }
        event.reactSuccess();
    }
}