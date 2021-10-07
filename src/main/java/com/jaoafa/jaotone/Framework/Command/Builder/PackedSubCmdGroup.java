package com.jaoafa.jaotone.Framework.Command.Builder;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandGroupData;

import java.util.ArrayList;
import java.util.function.Function;

public record PackedSubCmdGroup(String name,
                                String description,
                                Function<Member, Boolean> checkPermission,
                                ArrayList<PackedSubCmd> packedSubCmds,
                                SubcommandGroupData subcommandGroupData) {
}
