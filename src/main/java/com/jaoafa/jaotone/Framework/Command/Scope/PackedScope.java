package com.jaoafa.jaotone.Framework.Command.Scope;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public record PackedScope(@NotNull String scope,
                          @NotNull ScopeType scopeType,
                          @NotNull ArrayList<String> guilds) {
}
