package com.jaoafa.jaotone.Framework.Action.Builder;

import com.jaoafa.jaotone.Framework.Action.ActionFunction;
import com.jaoafa.jaotone.Framework.Action.ActionRouter;
import net.dv8tion.jda.api.entities.Member;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;
import java.util.function.Function;

public class BuildAction {
    //name:literal
    //private変数にsetで代入していって最終的にbuild()とかでrecordにまとめて渡す
    //ActionとLiteralどっちにもCheckPermissionセットできるけど、両方ある場合はLiteral側優先
    private final String name;
    private PackedLiteral[] literals = null;
    private Function<Member, Boolean> checkPermission = null;
    private ActionFunction function = null;

    public BuildAction(@NotNull String name) {
        this.name = name;
    }

    public BuildAction addLiterals(@NotNull PackedLiteral... literal) {
        this.literals = literal;
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
            for (PackedLiteral literal : literals) {
                ActionRouter.routeList.add(new ActionRouter.ActionRoutingData(
                        name,
                        literal.name(),
                        Optional.ofNullable(literal.checkPermission()).orElse(checkPermission),
                        literal.function()
                ));
            }
        if (function != null)
            ActionRouter.routeList.add(new ActionRouter.ActionRoutingData(name, null, checkPermission, function));

        return new PackedAction(name,literals,checkPermission,function);
    }
}
