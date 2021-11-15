package com.jaoafa.jaotone.Framework.Command.Builder;

import com.jaoafa.jaotone.Framework.Command.CmdFunction;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import java.util.ArrayList;

public record PackedCmd(String emoji,
                        String name,
                        ArrayList<String> names,
                        String description,
                        CmdFunction function,
                        ArrayList<OptionData> options,
                        ArrayList<String> aliases,
                        String scope,
                        ArrayList<PackedSubCmd> subCmds,
                        ArrayList<PackedSubCmdGroup> subCmdGroups,
                        ArrayList<CommandData> commandData) {
}
