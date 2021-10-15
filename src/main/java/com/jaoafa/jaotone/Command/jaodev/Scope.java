package com.jaoafa.jaotone.Command.jaodev;

import com.jaoafa.jaotone.Framework.Command.Scope.PackedScope;
import com.jaoafa.jaotone.Framework.Command.Scope.ScopeBuilder;
import com.jaoafa.jaotone.Framework.Command.Scope.ScopeSubstrate;
import com.jaoafa.jaotone.Framework.Command.Scope.ScopeType;

public class Scope implements ScopeSubstrate {
    @Override
    public PackedScope scope() {
        return new ScopeBuilder(this.getClass().getPackageName(), ScopeType.Private)
                .addGuilds("840578119467401257")
                .build();
    }
}
