package com.jaoafa.jaotone.Lib.Discord;

import java.util.HashMap;
import java.util.Map;

public class LibPrefix {
    private static final Map<String, String> prefixRecord = new HashMap<>();

    public static void setPrefix(String guildId, String prefix) {
        prefixRecord.put(guildId, prefix);
    }

    public static String getPrefix(String guildId) {
        return prefixRecord.get(guildId);
    }
}
