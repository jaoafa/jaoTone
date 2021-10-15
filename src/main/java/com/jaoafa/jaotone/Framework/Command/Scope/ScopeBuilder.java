package com.jaoafa.jaotone.Framework.Command.Scope;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;

public class ScopeBuilder {
    //required
    public String name;
    public ScopeType type;
    public ArrayList<String> guilds = new ArrayList<>();

    public ScopeBuilder(@NotNull String packageName, @NotNull ScopeType type) {
        String[] packagePath = packageName.split("\\.");
        this.name = packagePath[packagePath.length - 1];
        this.type = type;
    }

    public ScopeBuilder addGuilds(@NotNull String... guildId) {
        this.guilds.addAll(Arrays.asList(guildId));
        return this;
    }

    public PackedScope build() {
        if (type.equals(ScopeType.Public) && !guilds.isEmpty())
            throw new UnsupportedOperationException("PublicではGuildを制限することは出来ません！");
        if (type.equals(ScopeType.Private) && guilds.isEmpty())
            throw new UnsupportedOperationException("PrivateはGuild制限の指定が必要です！");

        return new PackedScope(this.name, this.type, this.guilds);
    }
}
