package com.jaoafa.jaotone.Framework.Command.Builder;

import com.jaoafa.jaotone.Framework.Command.CmdFunction;
import com.jaoafa.jaotone.Framework.Command.CmdRouter;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.function.Function;

public class BuildCmd {
    public CommandData commandData;

    //required
    public String name;
    public String description;

    //optional
    public CmdFunction function = null;
    public ArrayList<OptionData> options = new ArrayList<>();
    public String scope = null;
    public Function<Member, Boolean> checkPermission = null;

    //subCmds/Groups
    public ArrayList<PackedSubCmd> subCmds = new ArrayList<>();
    public ArrayList<PackedSubCmdGroup> subCmdGroups = new ArrayList<>();

    public ArrayList<CmdRouter.CmdRoutingData> queuedRoutingData = new ArrayList<>();

    public BuildCmd(@NotNull String name, @NotNull String description) {
        this.name = name;
        this.description = description;
    }

    public BuildCmd setFunction(@NotNull CmdFunction function) {
        this.function = function;
        return this;
    }

    public BuildCmd addOptions(@NotNull OptionData... options) {
        this.options.addAll(Arrays.asList(options));
        return this;
    }

    public BuildCmd setScope(@NotNull String scope) {
        this.scope = scope;
        return this;
    }

    public BuildCmd setPermCheck(@NotNull Function<@NotNull Member, @NotNull Boolean> checkPermission) {
        this.checkPermission = checkPermission;
        return this;
    }

    public BuildCmd addSubCmds(@NotNull PackedSubCmd... subCmds) {
        this.subCmds.addAll(Arrays.asList(subCmds));
        return this;
    }

    public BuildCmd addSubCmdGroups(@NotNull PackedSubCmdGroup... subCmdGroups) {
        this.subCmdGroups.addAll(Arrays.asList(subCmdGroups));
        return this;
    }

    public PackedCmd build() {
        this.commandData = new CommandData(this.name, this.description);

        if (!options.isEmpty()) this.commandData.addOptions(options);

        if (subCmdGroups.isEmpty() && subCmds.isEmpty()) {
            this.queuedRoutingData.add(new CmdRouter.CmdRoutingData(
                    this.name,
                    null,
                    null,
                    this.scope,
                    new ArrayList<>() {{
                        add(checkPermission);
                    }},
                    this.function,
                    this.options
            ));
        }
        if (subCmdGroups.isEmpty() && !subCmds.isEmpty()) {
            for (PackedSubCmd subCmd : subCmds) {
                this.commandData.addSubcommands(subCmd.subcommandData());

                this.queuedRoutingData.add(new CmdRouter.CmdRoutingData(
                        this.name,
                        null,
                        subCmd.name(),
                        this.scope,
                        new ArrayList<>() {{
                            add(checkPermission);
                            add(subCmd.checkPermission());
                        }},
                        subCmd.function(),
                        subCmd.options()
                ));
            }
        }
        if (!subCmdGroups.isEmpty() && subCmds.isEmpty()) {
            for (PackedSubCmdGroup subCmdGroup : subCmdGroups) {
                for (PackedSubCmd subCmd : subCmdGroup.packedSubCmds()) {
                    subCmdGroup.subcommandGroupData().addSubcommands(subCmd.subcommandData());

                    this.queuedRoutingData.add(new CmdRouter.CmdRoutingData(
                            this.name,
                            subCmdGroup.name(),
                            subCmd.name(),
                            this.scope,
                            new ArrayList<>() {{
                                add(checkPermission);
                                add(subCmdGroup.checkPermission());
                                add(subCmd.checkPermission());
                            }},
                            subCmd.function(),
                            subCmd.options()
                    ));
                }
                this.commandData.addSubcommandGroups(subCmdGroup.subcommandGroupData());
            }
        }


        CmdRouter.routeList.addAll(this.queuedRoutingData);

        return new PackedCmd(
                this.name,
                this.description,
                this.function,
                this.options,
                this.scope,
                this.subCmds,
                this.subCmdGroups,
                this.commandData
        );
    }
}
