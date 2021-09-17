package com.jaoafa.jaotone.Framework.Command.Builder;

import com.jaoafa.jaotone.Framework.Command.CmdFunction;
import com.jaoafa.jaotone.Framework.Command.CmdRouter;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandGroupData;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;

public class BuildCmd {
    public CommandData commandData;

    public String name;
    public String description;

    public CmdRouter.CmdRoutingData cmdRoutingData;

    private boolean lock;

    /**
     * コマンドを単体でビルドします (ファンクションあり)
     *
     * @param name        コマンドの名前
     * @param description コマンドの詳細
     * @param function    コマンドを実行するファンクション
     * @param options     オプション
     */
    public BuildCmd(@NotNull String name, @NotNull String description,
                    @NotNull CmdFunction function, @Nullable OptionData... options) {
        buildIndex(name, description, function, options);
        CmdRouter.routeList.add(cmdRoutingData);
    }

    /**
     * コマンドをビルドします (ファンクション無し)
     * サブコマンドかグループが追加される必要があります
     *
     * @param name        コマンドの名前
     * @param description コマンドの詳細
     * @param options     オプション
     */
    public BuildCmd(@NotNull String name, @NotNull String description, @Nullable OptionData... options) {
        buildIndex(name, description, null, options);
    }

    private void buildIndex(@NotNull String name, @NotNull String description,
                            @Nullable CmdFunction function, @Nullable OptionData... options) {
        this.name = name;
        this.description = description;
        this.commandData = new CommandData(name, description);

        if (options.length != 0) {
            this.commandData.addOptions(options);
        }

        cmdRoutingData = new CmdRouter.CmdRoutingData(
                name, null, null, function, options
        );
    }

    /**
     * サブコマンドを追加し、ビルドします。
     *
     * @param subCommands サブコマンドのデータ
     * @return BuildCmd
     */
    public BuildCmd buildWithSubCmd(BuildSubCmd... subCommands) {
        if (lock) return this;

        ArrayList<SubcommandData> subcommandData = new ArrayList<>();
        for (BuildSubCmd subCommand : subCommands) {
            CmdRouter.CmdRoutingData subCmdRoutingData = subCommand.routingData;
            CmdRouter.routeList.add(new CmdRouter.CmdRoutingData(
                    name,
                    subCmdRoutingData.groupName(),
                    subCmdRoutingData.subCmdName(),
                    subCmdRoutingData.function(),
                    subCmdRoutingData.optionData()
            ));
            subcommandData.add(subCommand.subCmdData);
        }
        commandData.addSubcommands(subcommandData);
        lock = true;
        return this;
    }

    /**
     * サブコマンドグループを追加し、ビルドします。
     *
     * @param groups グループのデータ
     * @return BuildCmd
     */
    public BuildCmd buildWithSubCmdGroup(BuildGroup... groups) {
        if (lock) return this;

        ArrayList<SubcommandGroupData> subcommandGroupData = new ArrayList<>();
        for (BuildGroup group : groups) {
            for (CmdRouter.CmdRoutingData subCmdRoutingData : group.cmdRoutingData) {
                CmdRouter.routeList.add(new CmdRouter.CmdRoutingData(
                        name,
                        subCmdRoutingData.groupName(),
                        subCmdRoutingData.subCmdName(),
                        subCmdRoutingData.function(),
                        subCmdRoutingData.optionData()
                ));
            }
            subcommandGroupData.add(group.data);
        }
        commandData.addSubcommandGroups(subcommandGroupData);

        lock = true;
        return this;
    }
}
