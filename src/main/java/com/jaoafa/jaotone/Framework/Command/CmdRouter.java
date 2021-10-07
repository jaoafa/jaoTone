package com.jaoafa.jaotone.Framework.Command;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import java.util.ArrayList;
import java.util.function.Function;

public class CmdRouter {
    public static ArrayList<CmdRoutingData> routeList = new ArrayList<>();

    public record CmdRoutingData(String cmdName,
                                 String groupName,
                                 String subCmdName,
                                 String scope,
                                 ArrayList<Function<Member, Boolean>> checkPermission,
                                 CmdFunction function,
                                 ArrayList<OptionData> optionData) {
    }
}
