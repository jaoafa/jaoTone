package com.jaoafa.jaotone.Framework.Command.App;

import com.jaoafa.jaotone.Framework.Command.CmdEventContainer;
import com.jaoafa.jaotone.Framework.Command.CmdOptionContainer;
import com.jaoafa.jaotone.Framework.Command.CmdType;
import com.jaoafa.jaotone.Framework.Lib.LibEmbedColor;
import com.jaoafa.jaotone.Framework.Lib.LibHookerChecks;
import com.jaoafa.jaotone.Framework.Lib.SupportedType;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

public class AppHooker extends ListenerAdapter {
    @Override
    public void onSlashCommand(@NotNull SlashCommandEvent event) {
        AppAnalysis.AppAnalysisResult result = AppAnalysis.analyzeAsApp(event);

        AppAnalysis.ExecutionErrorType errorType = result.errorType();
        if (!errorType.equals(AppAnalysis.ExecutionErrorType.NoError)) {
            String errorMessage = switch (errorType) {
                case CommandNotFound -> "コマンドが見つかりませんでした";
                case InvalidOptionName -> "オプション名が不正です"; //起こらないけど
                default -> "";
            };

            event.replyEmbeds(new EmbedBuilder()
                    .setTitle("## NOT FOUND ##")
                    .setDescription(errorMessage)
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
                event.replyEmbeds(checksResult.embed()).queue();
                return;
            }
        }

        result.routingData().function().execute(
                event.getJDA(),
                channelType.equals(ChannelType.TEXT) ? event.getGuild() : null,
                event.getChannel(),
                event.getChannelType(),
                channelType.equals(ChannelType.TEXT) ? event.getMember() : null,
                event.getUser(),
                new CmdOptionContainer(result.optionIndices()),
                new CmdEventContainer(event, null, CmdType.App)
        );
    }
}
