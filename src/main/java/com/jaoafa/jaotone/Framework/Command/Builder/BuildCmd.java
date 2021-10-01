package com.jaoafa.jaotone.Framework.Command.Builder;

import com.jaoafa.jaotone.Framework.Command.CmdFunction;
import com.jaoafa.jaotone.Framework.Command.CmdRouter;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandGroupData;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.function.Function;

public class BuildCmd {
    //Function<Member,Boolean>とかでPermission制限
    public CommandData commandData;

    public String name;
    public String description;

    public ArrayList<CmdRouter.CmdRoutingData> queuedRoutingData = new ArrayList<>();

    public String scope = null;

    public Function<Member, Boolean> checkPermission = null;

    private boolean locked;

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
    }

    /**
     * コマンドを単体でビルドします (権限チェックあり・ファンクションあり)
     *
     * @param name        コマンドの名前
     * @param description コマンドの詳細
     * @param function    コマンドを実行するファンクション
     * @param options     オプション
     */
    public BuildCmd(@NotNull String name, @NotNull String description,
                    @NotNull CmdFunction function, @Nullable Function<Member, Boolean> checkPermission, @Nullable OptionData... options) {
        this.checkPermission = checkPermission;
        buildIndex(name, description, function, options);
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

    /**
     * コマンドをビルドします (権限チェックあり・ファンクション無し)
     * サブコマンドかグループが追加される必要があります
     *
     * @param name        コマンドの名前
     * @param description コマンドの詳細
     * @param options     オプション
     */
    public BuildCmd(@NotNull String name, @NotNull String description, @Nullable Function<Member, Boolean> checkPermission, @Nullable OptionData... options) {
        this.checkPermission = checkPermission;
        buildIndex(name, description, null, options);
    }

    private void buildIndex(@NotNull String name, @NotNull String description,
                            @Nullable CmdFunction function, @Nullable OptionData... options) {
        this.name = name;
        this.description = description;
        this.commandData = new CommandData(name, description);
        if (options.length != 0) this.commandData.addOptions(options);


        queuedRoutingData.add(new CmdRouter.CmdRoutingData(
                name, null, null, scope, checkPermission, function, options
        ));
    }

    /**
     * サブコマンドを追加し、ビルドします。
     *
     * @param subCommands サブコマンドのデータ
     * @return This
     */
    public BuildCmd buildWithSubCmd(BuildSubCmd... subCommands) {
        if (locked) return this;

        ArrayList<SubcommandData> subcommandData = new ArrayList<>();
        queuedRoutingData = new ArrayList<>();
        for (BuildSubCmd subCommand : subCommands) {
            CmdRouter.CmdRoutingData subCmdRoutingData = subCommand.routingData;
            queuedRoutingData.add(new CmdRouter.CmdRoutingData(
                    name,
                    subCmdRoutingData.groupName(),
                    subCmdRoutingData.subCmdName(),
                    scope,
                    checkPermission,
                    subCmdRoutingData.function(),
                    subCmdRoutingData.optionData()
            ));
            subcommandData.add(subCommand.subCmdData);
        }
        commandData.addSubcommands(subcommandData);
        locked = true;
        return this;
    }

    /**
     * サブコマンドグループを追加し、ビルドします。
     *
     * @param groups グループのデータ
     * @return This
     */
    public BuildCmd buildWithSubCmdGroup(BuildGroup... groups) {
        if (locked) return this;

        ArrayList<SubcommandGroupData> subcommandGroupData = new ArrayList<>();
        queuedRoutingData = new ArrayList<>();
        for (BuildGroup group : groups) {
            for (CmdRouter.CmdRoutingData subCmdRoutingData : group.cmdRoutingData) {
                queuedRoutingData.add(new CmdRouter.CmdRoutingData(
                        name,
                        subCmdRoutingData.groupName(),
                        subCmdRoutingData.subCmdName(),
                        scope,
                        checkPermission,
                        subCmdRoutingData.function(),
                        subCmdRoutingData.optionData()
                ));
            }
            subcommandGroupData.add(group.data);
        }
        commandData.addSubcommandGroups(subcommandGroupData);

        locked = true;
        return this;
    }

    public BuildCmd finalBuild() {
        CmdRouter.routeList.addAll(queuedRoutingData);
        return this;
    }
}
