package com.jaoafa.jaotone.Framework.Command.Builder;

import com.jaoafa.jaotone.Framework.Command.CmdRouter;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandGroupData;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class BuildGroup {
    public BuildSubCmd[] subCommands;
    public ArrayList<SubcommandData> subcommandData = new ArrayList<>();
    public ArrayList<CmdRouter.CmdRoutingData> cmdRoutingData = new ArrayList<>();

    public SubcommandGroupData data;

    public String name;
    public String description;

    public BuildGroup(@NotNull String name, @NotNull String description) {
        this.name = name;
        this.description = description;

        data = new SubcommandGroupData(name, description);
    }

    public BuildGroup addSubCmd(BuildSubCmd... subCommands) {
        this.subCommands = subCommands;

        for (BuildSubCmd subCommand : subCommands) {
            CmdRouter.CmdRoutingData subCmdRoutingData = subCommand.routingData;
            cmdRoutingData.add(new CmdRouter.CmdRoutingData(
                    subCmdRoutingData.cmdName(),
                    name,
                    subCmdRoutingData.subCmdName(),
                    null,
                    null,
                    subCmdRoutingData.function(),
                    subCmdRoutingData.optionData()
            ));
            subcommandData.add(subCommand.subCmdData);
        }
        data.addSubcommands(subcommandData);
        return this;
    }
}
