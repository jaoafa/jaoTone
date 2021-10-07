package com.jaoafa.jaotone.Framework.Command.Builder;

import com.jaoafa.jaotone.Framework.Command.CmdFunction;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.function.Function;

public class BuildSubCmd {
    public SubcommandData subCmdData;

    //required
    public String name;
    public String description;
    public CmdFunction function;
    //optional
    public ArrayList<OptionData> options = new ArrayList<>();
    public Function<Member, Boolean> checkPermission = null;

    public BuildSubCmd(@NotNull String name, @NotNull String description) {
        this.name = name;
        this.description = description;
    }

    public BuildSubCmd setFunction(@NotNull CmdFunction function) {
        this.function = function;
        return this;
    }

    public BuildSubCmd addOptions(@NotNull OptionData... options) {
        this.options.addAll(Arrays.asList(options));
        return this;
    }

    public BuildSubCmd setPermCheck(@NotNull Function<@NotNull Member, @NotNull Boolean> checkPermission) {
        this.checkPermission = checkPermission;
        return this;
    }

    public PackedSubCmd build() {
        this.subCmdData = new SubcommandData(this.name, this.description);
        if (!options.isEmpty()) this.subCmdData.addOptions(this.options);

        return new PackedSubCmd(
                this.name,
                this.description,
                this.function,
                this.options,
                this.checkPermission,
                this.subCmdData
        );
    }
}
