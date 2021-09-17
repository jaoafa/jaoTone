package com.jaoafa.jaotone.Framework.Command.Text;

import com.jaoafa.jaotone.Framework.Command.CmdEventContainer;
import com.jaoafa.jaotone.Framework.Command.CmdOptionContainer;
import com.jaoafa.jaotone.Framework.Command.CmdType;
import com.jaoafa.jaotone.Lib.Discord.LibEmbedColor;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

public class TextHooker extends ListenerAdapter {
    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {
        if (!event.getMessage().getContentRaw().startsWith("^")) return;

        TextAnalysis.TextAnalysisResult result = TextAnalysis.analyzeAsText(event.getMessage().getContentRaw());

        TextAnalysis.ExecutionErrorType errorType = result.errorType();

        if (!errorType.equals(TextAnalysis.ExecutionErrorType.NoError)) {
            String errorMessage = switch (errorType) {
                case CommandNotFound -> "コマンドが見つかりませんでした";
                case InvalidOptionName -> "オプション名が不正です";
                case InvalidOptionType -> "オプションタイプが不正です";
                case MixedOptionForm -> "オプション記述が不正です";
                case NotEnoughOptions -> "必須オプションが足りません";
                default -> "";
            };

            event.getMessage().replyEmbeds(new EmbedBuilder()
                    .setTitle("## ERROR ##")
                    .setDescription(errorMessage)
                    .setColor(LibEmbedColor.failure)
                    .build()
            ).queue();
            return;
        }

        result.routingData().function().execute(
                event.getJDA(),
                event.getGuild(),
                event.getChannel(),
                event.getChannelType(),
                event.getMember(),
                event.getAuthor(),
                new CmdOptionContainer(result.optionIndices()),
                new CmdEventContainer(null, event, CmdType.Text)
        );
    }
}
