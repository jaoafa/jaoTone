package com.jaoafa.jaotone.command;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import com.jaoafa.jaotone.lib.ToneLib;

/**
 * コマンド: stop
 * <p>
 * コマンド実行者が参加しているボイスチャンネルに参加します。
 */
public class Cmd_Summon extends Command {
    /**
     * {@link Cmd_Summon} クラスの新しいインスタンスを初期化します。
     */
    @SuppressWarnings("unused")
    public Cmd_Summon() {
        this.name = "summon";
        this.help = "コマンド実行者が参加しているボイスチャンネルに参加します。";
        this.arguments = "";
    }

    @Override
    protected void execute(CommandEvent event) {
        if (event.getMember().getVoiceState() == null || !event.getMember().getVoiceState().inAudioChannel()) {
            ToneLib.replyError(event, "ボイスチャンネルに参加していません。");
            return;
        }
        event.getGuild().getAudioManager().openAudioConnection(event.getMember().getVoiceState().getChannel());
        event.reactSuccess();
    }
}
