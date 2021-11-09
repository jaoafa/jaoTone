package com.jaoafa.jaotone.Command.Public;

import com.jaoafa.jaotone.Framework.Command.Builder.BuildCmd;
import com.jaoafa.jaotone.Framework.Command.Builder.PackedCmd;
import com.jaoafa.jaotone.Framework.Command.CmdEventContainer;
import com.jaoafa.jaotone.Framework.Command.CmdOptionContainer;
import com.jaoafa.jaotone.Framework.Command.CmdSubstrate;
import com.jaoafa.jaotone.Framework.Lib.LibReply;
import com.jaoafa.jaotone.Framework.Lib.SupportedType;
import com.jaoafa.jaotone.Lib.jaoTone.LibAutoControl;
import com.jaoafa.jaotone.Lib.jaoTone.LibControl;
import com.jaoafa.jaotone.Lib.jaoTone.Music.LibPlayer;
import com.jaoafa.jaotone.Lib.jaoTone.Music.LibTrackData;
import com.jaoafa.jaotone.Lib.jaoTone.Music.LibVideo;
import com.jaoafa.jaotone.Lib.jaoTone.Music.LibVideoInfo;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

public class Cmd_Play implements CmdSubstrate {
    @Override
    public PackedCmd command() {
        return new BuildCmd("arrow_forward", "play", "音楽を再生します")
                .setSupportFor(SupportedType.TEXT)
                .setFunction(this::play)
                .addOptions(new OptionData(OptionType.STRING, "url", "URLを入力", true),
                        new OptionData(OptionType.STRING, "platform", "プラットフォームを指定します", false)
                                .addChoice("YouTube", "YouTube")
                                .addChoice("HTTP", "HTTP")
                                .addChoice("Local", "Local"),
                        new OptionData(OptionType.STRING, "loop", "ループの種類を指定します", false)
                                .addChoice("QueueLoop", "QueueLoop")
                                .addChoice("SingleLoop", "QueueLoop")
                                .addChoice("NoLoop", "NoLoop")
                )
                .build();
    }

    private void play(JDA jda, Guild guild, MessageChannel channel, ChannelType channelType, Member member, User user, CmdOptionContainer options, CmdEventContainer events) {
        LibControl.ControlResult result = LibAutoControl.join(member);
        if (!result.resultType().equals(LibControl.ControlResultType.AlreadyJoined))
            LibReply.replyEmbeds(events, result.embed().build()).done().queue();
        
        String urlUserInput = options.get("url").getAsString();
        String platformUserInput = options.getOrDefault("platform", "YouTube").getAsString();
        LibTrackData.PlatformFlag platformFlag = LibTrackData.PlatformFlag.valueOf(platformUserInput);
        LibTrackData.LoopFlag loopFlag = LibTrackData.LoopFlag.valueOf(options.getOrDefault("loop", "NoLoop").getAsString());
        LibVideoInfo videoInfo = null;

        String url = switch (platformFlag) {
            case YouTube -> {
                videoInfo = LibVideo.searchYouTube(urlUserInput, 1).videoInfos().get(0);
                yield videoInfo.videoURL();
            }
            case HTTP -> {
                videoInfo = LibVideo.searchHttp(urlUserInput).videoInfos().get(0);
                yield urlUserInput;
            }
            case Local -> {
                videoInfo = LibVideo.searchLocal(urlUserInput).videoInfos().get(0);
                yield urlUserInput;
            }
            //default -> {
            //    LibReply.replyEmbeds(events, new EmbedBuilder()
            //            .setTitle(":woman_bowing: そのようなプラットフォームには対応していません！")
            //            .setDescription("`YouTube`,`HTTP`,`Local` にのみ対応しています。")
            //            .setColor(LibEmbedColor.FAILURE)
            //            .build()
            //    ).done().queue();
            //
            //    yield null;
            //}
        };

        if (url == null) return;

        LibPlayer.getINSTANCE().loadAndPlay(
                (TextChannel) channel,
                url,
                new LibTrackData(
                        loopFlag,
                        platformFlag,
                        true,
                        videoInfo
                )
        );
    }
}
