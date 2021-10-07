package com.jaoafa.jaotone.Framework.Command.App;

import com.jaoafa.jaotone.Framework.Command.CmdEventContainer;
import com.jaoafa.jaotone.Framework.Command.CmdOptionContainer;
import com.jaoafa.jaotone.Framework.Command.CmdType;
import com.jaoafa.jaotone.Lib.Discord.LibEmbedColor;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.function.Function;

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

        ArrayList<Function<Member, Boolean>> checkPermissions = result.routingData().checkPermission();


        boolean isAllowed = true;

        for (Boolean checkPermResult :
                new ArrayList<Boolean>() {{
                    for (Function<Member, Boolean> checkPermission : checkPermissions)
                        if (checkPermission != null) add(checkPermission.apply(event.getMember()));
                }})
            if (!checkPermResult) {
                isAllowed = false;
                break;
            }


        if (!isAllowed) {
            event.replyEmbeds(new EmbedBuilder()
                    .setTitle("## NOT PERMITTED ##")
                    .setDescription("権限がありません")
                    .setColor(LibEmbedColor.FAILURE)
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
                event.getUser(),
                new CmdOptionContainer(result.optionIndices()),
                new CmdEventContainer(event, null, CmdType.App)
        );
    }
}
