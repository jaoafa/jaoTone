package com.jaoafa.jaotone.Lib.jaoTone;

import com.jaoafa.jaotone.Framework.Lib.LibEmbedColor;
import com.jaoafa.jaotone.Framework.Lib.LibEmbedPreset;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.VoiceChannel;

import java.util.List;

public class LibAutoControl {
    public static LibControl.ControlResult join(Member member) {
        if (member.getVoiceState() == null)
            return new LibControl.ControlResult(LibControl.ControlResultType.Failure, LibEmbedPreset
                    .getInfoFailed(null, "GET MEMBER VOICESTATE FAILED"));

        Guild guild = member.getGuild();
        Member self = guild.getMember(LibValue.jda.getSelfUser());

        if (self == null)
            return new LibControl.ControlResult(LibControl.ControlResultType.Failure, LibEmbedPreset
                    .getInfoFailed(null, "GET SELF FAILED"));

        GuildVoiceState selfVoiceState = self.getVoiceState();

        if (selfVoiceState == null)
            return new LibControl.ControlResult(LibControl.ControlResultType.Failure, LibEmbedPreset
                    .getInfoFailed(null, "GET SELF VOICESTATE FAILED"));

        VoiceChannel connectTo = member.getVoiceState().getChannel();
        if (connectTo != null) {
            LibControl.ControlResult result = LibControl.join(connectTo);
            return new LibControl.ControlResult(result.resultType(), result.embed());
        } else if (!selfVoiceState.inVoiceChannel()) {
            List<VoiceChannel> voiceChannelList = guild.getVoiceChannels();
            VoiceChannel maxVoiceChannel = voiceChannelList.get(0);

            for (VoiceChannel voiceChannel : voiceChannelList)
                if (maxVoiceChannel.getMembers().size() < voiceChannel.getMembers().size())
                    maxVoiceChannel = voiceChannel;

            LibControl.ControlResult result = LibControl.join(maxVoiceChannel);

            if (result.resultType().equals(LibControl.ControlResultType.Failure))
                return new LibControl.ControlResult(result.resultType(), result.embed());

            return new LibControl.ControlResult(LibControl.ControlResultType.Joined, new EmbedBuilder()
                    .setTitle(":inbox_tray: 自動接続しました")
                    .setDescription("一番人数が多かった <#%s> に接続しました".formatted(maxVoiceChannel.getId()))
                    .setColor(LibEmbedColor.SUCCESS));
        }

        return new LibControl.ControlResult(LibControl.ControlResultType.AlreadyJoined, new EmbedBuilder()
                .setTitle(":grey_question: 既に接続しています")
                .setColor(LibEmbedColor.SUCCESS));
    }
}
