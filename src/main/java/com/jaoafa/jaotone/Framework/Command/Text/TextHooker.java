package com.jaoafa.jaotone.Framework.Command.Text;

import com.jaoafa.jaotone.Framework.Command.CmdEventContainer;
import com.jaoafa.jaotone.Framework.Command.CmdOptionContainer;
import com.jaoafa.jaotone.Framework.Command.CmdType;
import com.jaoafa.jaotone.Framework.Lib.LibEmbedColor;
import com.jaoafa.jaotone.Framework.Lib.LibHookerChecks;
import com.jaoafa.jaotone.Framework.Lib.LibPrefix;
import com.jaoafa.jaotone.Framework.Lib.SupportedType;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

public class TextHooker extends ListenerAdapter {
    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {
        if (!event.getChannelType().equals(ChannelType.TEXT)) {
            event.getMessage().replyEmbeds(new EmbedBuilder()
                    .setTitle("## ERROR ##")
                    .setDescription("テキストチャンネル以外では利用できません！")
                    .setColor(LibEmbedColor.FAILURE)
                    .build()
            ).queue();
            return;
        }

        String guildId = event.getGuild().getId();

        if (LibPrefix.getPrefix(guildId) == null)
            LibPrefix.setPrefix(guildId, "^");

        if (!event.getMessage().getContentRaw().startsWith(
                LibPrefix.getPrefix(guildId)
        )) return;

        TextAnalysis.TextAnalysisResult result =
                TextAnalysis.analyzeAsText(guildId, event.getMessage().getContentRaw());
        TextAnalysis.ExecutionErrorType errorType = result.errorType();

        if (!errorType.equals(TextAnalysis.ExecutionErrorType.NoError)) {
            event.getMessage().replyEmbeds(new EmbedBuilder()
                    .setTitle("## ERROR ##")
                    .setDescription(switch (errorType) {
                        case CommandNotFound -> "コマンドが見つかりませんでした";
                        case InvalidOptionName -> "オプション名が不正です";
                        case InvalidOptionType -> "オプションタイプが不正です";
                        case MixedOptionForm -> "オプション記述が不正です";
                        case NotEnoughOptions -> "必須オプションが足りません";
                        default -> "";
                    })
                    .setColor(LibEmbedColor.FAILURE)
                    .build()
            ).queue();
            return;
        }

        ChannelType channelType = event.getChannelType();
        LibHookerChecks.ChecksResult checksResult = LibHookerChecks.check(
                channelType,
                result.routingData().supportedType(),
                channelType.equals(ChannelType.TEXT) ? event.getMember() : null,
                result.routingData().checkPermission()
        );

        switch (checksResult.resultType()) {
            case NotAllowed, ChannelTypeNotSupported -> {
                event.getMessage().replyEmbeds(checksResult.embed()).queue();
                return;
            }
        }

        result.routingData().function().execute(
                event.getJDA(),
                channelType.equals(ChannelType.TEXT) ? event.getGuild() : null,
                event.getChannel(),
                event.getChannelType(),
                channelType.equals(ChannelType.TEXT) ? event.getMember() : null,
                event.getAuthor(),
                new CmdOptionContainer(result.optionIndices()),
                new CmdEventContainer(null, event, CmdType.Text)
        );
    }
}
