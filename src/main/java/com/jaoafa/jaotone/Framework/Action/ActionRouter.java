package com.jaoafa.jaotone.Framework.Action;

import net.dv8tion.jda.api.entities.Member;

import java.util.ArrayList;
import java.util.function.Function;

public class ActionRouter {
    public static ArrayList<ActionRoutingData> routeList = new ArrayList<>();

    public record ActionRoutingData(String actionName,
                                    String literalName,
                                    Function<Member, Boolean> checkPermission,
                                    ActionFunction function) {
    }
}
