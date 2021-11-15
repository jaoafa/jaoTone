package com.jaoafa.jaotone.Lib.jaoTone;

import com.jaoafa.jaotone.Framework.Lib.LibEmbedColor;
import com.jaoafa.jaotone.Framework.Lib.LibEmbedPreset;
import com.jaoafa.jaotone.Lib.jaoTone.Music.LibPlayer;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.managers.AudioManager;

public class LibControl {
    //あとでまとめる
    //Enum JOIN/LEAVE とかで一つのメソッドに (AutoControlも)
    public static ControlResult join(VoiceChannel connectTo) {
        if (connectTo == null)
            return new ControlResult(
                    ControlResultType.Failure,
                    LibEmbedPreset.getInfoFailed(null, "GET VOICECHANNEL FAILED")
            );

        Guild guild = connectTo.getGuild();
        Member self = guild.getMember(LibValue.jda.getSelfUser());
        if (self == null)
            return new ControlResult(
                    ControlResultType.Failure,
                    LibEmbedPreset.getInfoFailed(null, "GET SELF FAILED")
            );

        GuildVoiceState voiceState = self.getVoiceState();
        if (voiceState == null)
            return new ControlResult(
                    ControlResultType.Failure,
                    LibEmbedPreset.getInfoFailed(null, "GET VOICESTATE FAILED")
            );

        EmbedBuilder successEmbed = new EmbedBuilder()
                .setDescription("**:inbox_tray: <#%s> に接続しました**".formatted(connectTo.getId()))
                .setColor(LibEmbedColor.SUCCESS);

        VoiceChannel connectedChannel = voiceState.getChannel();

        if (connectTo.equals(connectedChannel))
            return new ControlResult(ControlResultType.AlreadyJoined, new EmbedBuilder()
                    .setDescription("**:grey_question: 既に <#%s> に接続しています**".formatted(connectedChannel.getId()))
                    .setColor(LibEmbedColor.NORMAL));

        AudioManager audioManager = guild.getAudioManager();
        audioManager.openAudioConnection(connectTo);

        if (connectedChannel == null)
            return new ControlResult(ControlResultType.Joined, successEmbed);
        else
            return new ControlResult(
                    ControlResultType.Moved,
                    successEmbed
                            .setDescription("<#%s> >> <#%s>"
                                    .formatted(
                                            connectedChannel.getId(),
                                            connectTo.getId()
                                    )
                            )
            );
    }

    public static ControlResult leave(Guild leaveFrom) {
        if (leaveFrom == null)
            return new ControlResult(
                    ControlResultType.Failure,
                    LibEmbedPreset.getInfoFailed(null, "GET VOICECHANNEL FAILED")
            );

        Member self = leaveFrom.getMember(LibValue.jda.getSelfUser());

        if (self == null)
            return new ControlResult(
                    ControlResultType.Failure,
                    LibEmbedPreset.getInfoFailed(null, "GET SELF FAILED")
            );

        GuildVoiceState selfVoiceState = self.getVoiceState();
        if (selfVoiceState == null)
            return new ControlResult(
                    ControlResultType.Failure,
                    LibEmbedPreset.getInfoFailed(null, "GET VOICESTATE FAILED")
            );

        if (!selfVoiceState.inVoiceChannel() || selfVoiceState.getChannel() == null)
            return new ControlResult(ControlResultType.AlreadyLeft, new EmbedBuilder()
                    .setDescription("**:grey_question: 既に切断しています**")
                    .setColor(LibEmbedColor.SUCCESS));

        LibPlayer.getINSTANCE().getGuildMusicManager(leaveFrom).player.destroy();
        leaveFrom.getAudioManager().closeAudioConnection();

        return new ControlResult(ControlResultType.Left, new EmbedBuilder()
                .setDescription("**:outbox_tray: <#%s> から切断しました**".formatted(selfVoiceState.getChannel().getId()))
                .setColor(LibEmbedColor.SUCCESS));
    }

    public record ControlResult(ControlResultType resultType,
                                EmbedBuilder embed) {
    }

    public enum ControlResultType {
        Joined, Left, Failure, AlreadyJoined, AlreadyLeft, Moved
    }
}
