package com.jaoafa.jaotone.Framework.Command.Builder;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandGroupData;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.function.Function;

public class BuildSubCmdGroup {
    public SubcommandGroupData subcommandGroupData;

    //required
    public String name;
    public String description;

    //optional
    public ArrayList<PackedSubCmd> packedSubCmds = new ArrayList<>();
    public Function<Member, Boolean> checkPermission = null;

    public BuildSubCmdGroup(@NotNull String name, @NotNull String description) {
        this.name = name;
        this.description = description;
    }

    public BuildSubCmdGroup addSubCmd(@NotNull PackedSubCmd... subCommands) {
        this.packedSubCmds.addAll(Arrays.asList(subCommands));
        return this;
    }

    public BuildSubCmdGroup setPermCheck(@NotNull Function<@NotNull Member, @NotNull Boolean> checkPermission) {
        this.checkPermission = checkPermission;
        return this;
    }

    public PackedSubCmdGroup build() {
        this.subcommandGroupData = new SubcommandGroupData(name, description);

        return new PackedSubCmdGroup(
                this.name,
                this.description,
                this.checkPermission,
                this.packedSubCmds,
                this.subcommandGroupData
        );
    }
}
