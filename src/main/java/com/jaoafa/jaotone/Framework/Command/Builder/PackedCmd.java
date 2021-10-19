package com.jaoafa.jaotone.Framework.Command.Builder;

import com.jaoafa.jaotone.Framework.Command.CmdFunction;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import java.util.ArrayList;

public record PackedCmd(String emoji,
                        String name,
                        String description,
                        CmdFunction function,
                        ArrayList<OptionData> options,
                        String scope,
                        ArrayList<PackedSubCmd> subCmds,
                        ArrayList<PackedSubCmdGroup> subCmdGroups,
                        CommandData commandData) {
}
