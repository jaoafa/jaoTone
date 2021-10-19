package com.jaoafa.jaotone.Framework.Lib;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageEmbed;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.function.Function;

public class LibHookerChecks {
    public static ChecksResult check(@NotNull ChannelType channelType,
                                     @Nullable SupportedType supportedType,
                                     @Nullable Member member,
                                     @NotNull ArrayList<Function<Member, Boolean>> checkPermissions) {
        if (supportedType == null) supportedType = SupportedType.ALL;
        if (!supportedType.equals(SupportedType.ALL) &&
                !supportedType.name().equals(channelType.name())) {
            return new ChecksResult(
                    ChecksResultType.ChannelTypeNotSupported,
                    new EmbedBuilder()
                            .setTitle("サポートされていません！")
                            .setDescription("この機能は %s チャンネルをサポートしていません。".formatted(supportedType.name()))
                            .build()
            );
        }

        if (channelType.equals(ChannelType.TEXT)) {
            boolean isAllowed = true;

            for (Boolean checkPermResult :
                    new ArrayList<Boolean>() {{
                        for (Function<Member, Boolean> checkPermission : checkPermissions)
                            if (checkPermission != null) add(checkPermission.apply(member));
                    }})
                if (!checkPermResult) {
                    isAllowed = false;
                    break;
                }

            if (!isAllowed)
                return new ChecksResult(
                        ChecksResultType.NotAllowed, new EmbedBuilder()
                        .setTitle("## NOT PERMITTED ##")
                        .setDescription("権限がありません")
                        .setColor(LibEmbedColor.FAILURE)
                        .build()
                );
        }
        return new ChecksResult(ChecksResultType.Passed, null);
    }

    public enum ChecksResultType {
        ChannelTypeNotSupported, NotAllowed, Passed
    }

    public record ChecksResult(ChecksResultType resultType, MessageEmbed embed) {
    }
}
