package com.jaoafa.jaotone.Framework.Action.Builder;

import com.jaoafa.jaotone.Framework.Action.ActionFunction;
import com.jaoafa.jaotone.Framework.Action.ActionRouter;
import com.jaoafa.jaotone.Framework.Lib.SupportedType;
import net.dv8tion.jda.api.entities.Member;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.function.Function;

public class BuildAction {
    private final String name;
    private ArrayList<PackedLiteral> literals = null;
    private SupportedType supportedType = null;
    private Function<Member, Boolean> checkPermission = null;
    private ActionFunction function = null;

    public BuildAction(@NotNull String name) {
        this.name = name;
    }

    public BuildAction setLiterals(@NotNull PackedLiteral... literal) {
        this.literals = new ArrayList<>(Arrays.asList(literal));
        return this;
    }

    public BuildAction setSupportFor(@NotNull SupportedType type) {
        this.supportedType = type;
        return this;
    }

    public BuildAction setPermCheck(@NotNull Function<Member, Boolean> checkPermission) {
        this.checkPermission = checkPermission;
        return this;
    }

    public BuildAction setFunction(@NotNull ActionFunction function) {
        this.function = function;
        return this;
    }


    public PackedAction build() {
        if (literals == null && function == null)
            throw new UnsupportedOperationException("Literal がない場合は Function を null にすることが出来ません!");
        if (literals != null && function != null)
            throw new UnsupportedOperationException("Literal と Function を共に使用することはできません!");

        if (literals != null)
            for (PackedLiteral literal : literals)
                ActionRouter.routeList.add(new ActionRouter.ActionRoutingData(
                        name,
                        literal.name(),
                        supportedType,
                        new ArrayList<>() {{
                            add(literal.checkPermission());
                            add(checkPermission);
                        }},
                        literal.function()
                ));
        if (function != null)
            ActionRouter.routeList.add(new ActionRouter.ActionRoutingData(
                    name,
                    null,
                    supportedType,
                    new ArrayList<>() {{
                        add(checkPermission);
                    }},
                    function
            ));

        return new PackedAction(name, literals, checkPermission, function);
    }
}
