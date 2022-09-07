package com.jaoafa.jaotone.commandtemp;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import com.jaoafa.jaotone.libtemp.ToneLib;
import com.jaoafa.jaotone.player.PlayerManager;

public class Cmd_Shuffle extends Command {
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
