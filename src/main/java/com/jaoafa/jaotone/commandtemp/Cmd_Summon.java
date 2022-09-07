package com.jaoafa.jaotone.commandtemp;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import com.jaoafa.jaotone.libtemp.ToneLib;

public class Cmd_Summon extends Command {
    @SuppressWarnings("unused")
    public Cmd_Summon() {
        this.name = "summon";
        this.help = "ボイスチャンネルに参加します。";
        this.arguments = "";
    }

    @Override
    protected void execute(CommandEvent event) {
        if (event.getMember().getVoiceState() == null || !event.getMember().getVoiceState().inAudioChannel()) {
            ToneLib.replyError(event, "ボイスチャンネルに参加していません。");
            return;
        }
        event.getGuild().getAudioManager().openAudioConnection(event.getMember().getVoiceState().getChannel());
        ToneLib.replySuccess(event, "ボイスチャンネルに参加しました。");
    }
}
