package com.jaoafa.jaotone.Lib.jaoTone;

import com.jaoafa.jaotone.Framework.Lib.LibEmbedColor;
import com.jaoafa.jaotone.Framework.Lib.LibEmbedPreset;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.managers.AudioManager;

public class LibControl {
    public static JoinResult join(VoiceChannel connectTo) {
        if (connectTo == null)
            return new JoinResult(
                    false,
                    LibEmbedPreset.getInfoFailed(null,"GET VOICECHANNEL FAILDED")
            );

        Guild guild = connectTo.getGuild();
        Member self = guild.getMember(LibValue.jda.getSelfUser());
        if (self == null)
            return new JoinResult(
                    false,
                    LibEmbedPreset.getInfoFailed(null,"GET SELF FAILED")
            );

        GuildVoiceState voiceState = self.getVoiceState();
        if (voiceState == null)
            return new JoinResult(
                    false,
                    LibEmbedPreset.getInfoFailed(null,"GET VOICESTATE FAILED")
            );

        EmbedBuilder successEmbed = new EmbedBuilder()
                .setTitle(":inbox_tray: <#%s> に接続しました".formatted(connectTo.getId()))
                .setColor(LibEmbedColor.SUCCESS);

        VoiceChannel connectedChannel = voiceState.getChannel();

        if (connectTo.equals(connectedChannel)) {
            return new JoinResult(true, successEmbed);
        }

        AudioManager audioManager = guild.getAudioManager();
        audioManager.openAudioConnection(connectTo);


        if (connectedChannel == null || connectTo.equals(connectedChannel))
            return new JoinResult(true, successEmbed);
        else
            return new JoinResult(
                    true,
                    successEmbed
                            .setDescription("<#%s> >> <#%s>"
                                    .formatted(
                                            connectedChannel.getId(),
                                            connectTo.getId()
                                    )
                            )
            );
    }

    public record JoinResult(boolean isSuccessful,
                             EmbedBuilder embed) {
    }
}
