package com.jaoafa.jaotone.Command.Public;

import com.jaoafa.jaotone.Framework.Command.Builder.BuildCmd;
import com.jaoafa.jaotone.Framework.Command.Builder.PackedCmd;
import com.jaoafa.jaotone.Framework.Command.CmdEventContainer;
import com.jaoafa.jaotone.Framework.Command.CmdOptionContainer;
import com.jaoafa.jaotone.Framework.Command.CmdSubstrate;
import com.jaoafa.jaotone.Framework.Lib.LibReply;
import com.jaoafa.jaotone.Framework.Lib.SupportedType;
import com.jaoafa.jaotone.Lib.jaoTone.LibAutoControl;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.*;

public class Cmd_Leave implements CmdSubstrate {
    @Override
    public PackedCmd command() {
        return new BuildCmd("outbox_tray", "leave", "切断します")
                .setSupportFor(SupportedType.TEXT)
                .setFunction(this::leave)
                .build();
    }

    private void leave(JDA jda, Guild guild, MessageChannel channel, ChannelType channelType, Member member, User user, CmdOptionContainer options, CmdEventContainer events) {
        LibReply.replyEmbeds(
                events,
                LibAutoControl
                        .leave(guild)
                        .embed()
                        .build()
        ).done().queue();
    }
}
