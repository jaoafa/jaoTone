package com.jaoafa.jaotone.Framework.Action.Builder;

import com.jaoafa.jaotone.Framework.Action.ActionFunction;
import net.dv8tion.jda.api.entities.Member;

import java.util.function.Function;

public record PackedAction(String name,
                           PackedLiteral[] literals,
                           Function<Member, Boolean> checkPermission,
                           ActionFunction function) {
}
