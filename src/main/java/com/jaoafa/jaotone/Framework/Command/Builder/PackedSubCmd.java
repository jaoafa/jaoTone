package com.jaoafa.jaotone.Framework.Command.Builder;

import com.jaoafa.jaotone.Framework.Command.CmdFunction;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData;

import java.util.ArrayList;
import java.util.function.Function;

public record PackedSubCmd(String name,
                           String description,
                           CmdFunction function,
                           ArrayList<OptionData> options,
                           Function<Member, Boolean> checkPermission,
                           SubcommandData subcommandData) {
}
