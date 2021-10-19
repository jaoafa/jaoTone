package com.jaoafa.jaotone.Framework.Action;

import com.jaoafa.jaotone.Framework.Lib.SupportedType;
import net.dv8tion.jda.api.entities.Member;

import java.util.ArrayList;
import java.util.function.Function;

public class ActionRouter {
    public static ArrayList<ActionRoutingData> routeList = new ArrayList<>();

    public record ActionRoutingData(String actionName,
                                    String literalName,
                                    SupportedType supportedType,
                                    ArrayList<Function<Member, Boolean>> checkPermission,
                                    ActionFunction function) {
    }
}
