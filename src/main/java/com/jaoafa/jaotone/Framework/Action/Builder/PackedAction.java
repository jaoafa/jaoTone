package com.jaoafa.jaotone.Framework.Action.Builder;

import com.jaoafa.jaotone.Framework.Action.ActionFunction;
import net.dv8tion.jda.api.entities.Member;

import java.util.ArrayList;
import java.util.function.Function;

public record PackedAction(String name,
                           ArrayList<PackedLiteral> literals,
                           Function<Member, Boolean> checkPermission,
                           ActionFunction function) {
}
