package com.jaoafa.jaotone.Framework.Command.Scope;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ScopeManager {
    // ScopeName / ScopeType / GuildID
    public static Table<@NotNull String, @NotNull ScopeType, @NotNull ArrayList<String>> scopes = HashBasedTable.create();

    //CmdName / ScopeName
    public static Map<String, String> scopeList = new HashMap<>();
}
