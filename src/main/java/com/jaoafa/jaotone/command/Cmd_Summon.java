package com.jaoafa.jaotone.command;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import com.jaoafa.jaotone.lib.ToneLib;
import net.dv8tion.jda.api.entities.channel.unions.AudioChannelUnion;

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
        // JDA v6 では inAudioChannel() が true でも getChannel() が null を返す可能性がある
        AudioChannelUnion channel = event.getMember().getVoiceState().getChannel();
        if (channel == null) {
            ToneLib.replyError(event, "ボイスチャンネルに参加していません。");
            return;
        }
        event.getGuild().getAudioManager().openAudioConnection(channel);
        event.reactSuccess();
    }
}
