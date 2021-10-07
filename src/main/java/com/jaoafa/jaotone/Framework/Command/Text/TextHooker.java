package com.jaoafa.jaotone.Framework.Command.Text;

import com.jaoafa.jaotone.Framework.Command.CmdEventContainer;
import com.jaoafa.jaotone.Framework.Command.CmdOptionContainer;
import com.jaoafa.jaotone.Framework.Command.CmdType;
import com.jaoafa.jaotone.Lib.Discord.LibEmbedColor;
import com.jaoafa.jaotone.Lib.Discord.LibPrefix;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.function.Function;

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

        ArrayList<Function<Member, Boolean>> checkPermissions = result.routingData().checkPermission();


        boolean isAllowed = true;

        for (Boolean checkPermResult :
                new ArrayList<Boolean>() {{
                    for (Function<Member, Boolean> checkPermission : checkPermissions)
                        add(checkPermission.apply(event.getMember()));
                }})
            if (!checkPermResult) {
                isAllowed = false;
                break;
            }


        if (!isAllowed) {
            event.getMessage().replyEmbeds(new EmbedBuilder()
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
                event.getAuthor(),
                new CmdOptionContainer(result.optionIndices()),
                new CmdEventContainer(null, event, CmdType.Text)
        );
    }
}
