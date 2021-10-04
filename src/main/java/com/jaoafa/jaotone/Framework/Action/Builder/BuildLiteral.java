package com.jaoafa.jaotone.Framework.Action.Builder;

import com.jaoafa.jaotone.Framework.Action.ActionFunction;
import net.dv8tion.jda.api.entities.Member;
import org.jetbrains.annotations.NotNull;

import java.util.function.Function;

public class BuildLiteral {
    private final String name;
    private Function<Member, Boolean> checkPermission = null;
    private ActionFunction function = null;

    public BuildLiteral(@NotNull String name) {
        this.name = name;
    }

    public BuildLiteral addPermCheck(@NotNull Function<Member, Boolean> checkPermission) {
        this.checkPermission = checkPermission;
        return this;
    }

    public BuildLiteral setFunction(@NotNull ActionFunction function) {
        this.function = function;
        return this;
    }

    public PackedLiteral build() {
        if (function == null)
            throw new UnsupportedOperationException("Literal の Function を null にすることは出来ません！");
        return new PackedLiteral(name,checkPermission,function);
    }
}