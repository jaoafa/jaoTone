package com.jaoafa.jaotone.Framework.Command.Builder;

import com.jaoafa.jaotone.Framework.Command.CmdFunction;
import com.jaoafa.jaotone.Framework.Command.CmdRouter;
import com.jaoafa.jaotone.Framework.Lib.SupportedType;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.function.Function;

public class BuildCmd {
    //required
    private final String emoji;
    private final String name;
    private final String description;
    public ArrayList<CommandData> commandData = new ArrayList<>();
    //subCmds/Groups
    public ArrayList<PackedSubCmd> subCmds = new ArrayList<>();
    public ArrayList<PackedSubCmdGroup> subCmdGroups = new ArrayList<>();
    public ArrayList<CmdRouter.CmdRoutingData> queuedRoutingData = new ArrayList<>();
    //optional
    private CmdFunction function = null;
    private SupportedType supportedType = null;
    private String scope = null;
    private Function<Member, Boolean> checkPermission = null;
    private final ArrayList<OptionData> options = new ArrayList<>();
    private final ArrayList<String> names = new ArrayList<>();

    public BuildCmd(@NotNull String emoji, @NotNull String name, @NotNull String description) {
        this.emoji = emoji;
        this.name = name;
        this.names.add(name);
        this.description = description;
    }

    public BuildCmd addAlias(@NotNull String... aliases) {
        this.names.addAll(Arrays.asList(aliases));
        return this;
    }

    public BuildCmd setSupportFor(@NotNull SupportedType type) {
        this.supportedType = type;
        return this;
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
        for (String name : names)
            this.commandData.add(new CommandData(name,
                    name.equals(this.name) ?
                            this.description :
                            this.description + " (%s)".formatted(this.name)
            ));

        if (!options.isEmpty())
            this.commandData.forEach(cmdData -> cmdData.addOptions(options));


        if (subCmdGroups.isEmpty() && subCmds.isEmpty())
            for (String name : names) {
                this.queuedRoutingData.add(new CmdRouter.CmdRoutingData(
                        name,
                        null,
                        null,
                        supportedType == null ? SupportedType.ALL : supportedType,
                        this.scope,
                        new ArrayList<>() {{
                            add(checkPermission);
                        }},
                        this.function,
                        this.options
                ));
            }


        if (subCmdGroups.isEmpty() && !subCmds.isEmpty())
            for (String name : names)
                for (PackedSubCmd subCmd : subCmds) {
                    this.commandData.forEach(cmdData -> cmdData.addSubcommands(subCmd.subcommandData()));

                    this.queuedRoutingData.add(new CmdRouter.CmdRoutingData(
                            name,
                            null,
                            subCmd.name(),
                            supportedType == null ? SupportedType.ALL : supportedType,
                            this.scope,
                            new ArrayList<>() {{
                                add(checkPermission);
                                add(subCmd.checkPermission());
                            }},
                            subCmd.function(),
                            subCmd.options()
                    ));
                }


        if (!subCmdGroups.isEmpty() && subCmds.isEmpty()) {
            for (String name : names)
                for (PackedSubCmdGroup subCmdGroup : subCmdGroups) {
                    for (PackedSubCmd subCmd : subCmdGroup.packedSubCmds()) {
                        subCmdGroup.subcommandGroupData().addSubcommands(subCmd.subcommandData());

                        this.queuedRoutingData.add(new CmdRouter.CmdRoutingData(
                                name,
                                subCmdGroup.name(),
                                subCmd.name(),
                                supportedType == null ? SupportedType.ALL : supportedType,
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
                    this.commandData.forEach(cmdData -> cmdData.addSubcommandGroups(subCmdGroup.subcommandGroupData()));
                }
        }


        CmdRouter.routeList.addAll(this.queuedRoutingData);

        return new PackedCmd(
                this.emoji,
                this.name,
                this.names,
                this.description,
                this.function,
                this.options,
                this.names,
                this.scope,
                this.subCmds,
                this.subCmdGroups,
                this.commandData
        );
    }
}
