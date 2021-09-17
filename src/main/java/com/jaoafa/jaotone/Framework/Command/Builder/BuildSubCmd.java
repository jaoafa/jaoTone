package com.jaoafa.jaotone.Framework.Command.Builder;

import com.jaoafa.jaotone.Framework.Command.CmdFunction;
import com.jaoafa.jaotone.Framework.Command.CmdRouter;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class BuildSubCmd {
    public SubcommandData subCmdData;
    public CmdRouter.CmdRoutingData routingData;

    public BuildSubCmd(@NotNull String name, @NotNull String description,
                       @NotNull CmdFunction function, @Nullable OptionData... options) {
        subCmdData = new SubcommandData(name, description);
        routingData = new CmdRouter.CmdRoutingData(null, null, name, function, options);

        if (options.length != 0) {
            this.subCmdData.addOptions(options);
        }
    }
}
