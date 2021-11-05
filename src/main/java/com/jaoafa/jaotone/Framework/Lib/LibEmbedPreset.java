package com.jaoafa.jaotone.Framework.Lib;

import net.dv8tion.jda.api.EmbedBuilder;

public class LibEmbedPreset {
    public static EmbedBuilder getInfoFailed(String localizedMessage, String message) {
        EmbedBuilder embedBuilder = new EmbedBuilder()
                .setTitle(":octagonal_sign: 情報を取得できませんでした")
                .setColor(LibEmbedColor.FAILURE);
        if (localizedMessage != null)
            embedBuilder.appendDescription(localizedMessage+"\n");
        if (message != null)
            embedBuilder.appendDescription("`%s`".formatted(message));
        return embedBuilder;
    }
}
