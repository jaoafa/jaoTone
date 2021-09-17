package com.jaoafa.jaotone.Framework.Command;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.*;

@FunctionalInterface
public interface CmdFunction {
    void execute(JDA jda,
                 Guild guild,
                 MessageChannel channel,
                 ChannelType type,
                 Member member,
                 User user,
                 CmdOptionContainer options,
                 CmdEventContainer events);
}
