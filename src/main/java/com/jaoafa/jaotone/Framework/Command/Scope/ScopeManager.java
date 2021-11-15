package com.jaoafa.jaotone.Framework.Command.Scope;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ScopeManager {
    // ScopeName / ScopeType / GuildID
    public record ScopeIndex(ScopeType type, ArrayList<String> guilds) {
    }

    public static Map<@NotNull String, @NotNull ScopeIndex> scopes = new HashMap<>();

    //CmdName / ScopeName
    public static Map<String, String> scopeList = new HashMap<>();
}
