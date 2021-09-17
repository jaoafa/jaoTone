package com.jaoafa.jaotone.Framework.Command;

import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import java.util.ArrayList;

public class CmdRouter {
    public static ArrayList<CmdRoutingData> routeList = new ArrayList<>();

    public record CmdRoutingData(String cmdName,
                                 String groupName,
                                 String subCmdName,
                                 CmdFunction function,
                                 OptionData... optionData) {
    }
}
