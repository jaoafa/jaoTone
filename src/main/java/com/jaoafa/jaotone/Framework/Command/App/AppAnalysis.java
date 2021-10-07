package com.jaoafa.jaotone.Framework.Command.App;

import com.jaoafa.jaotone.Framework.Command.CmdOptionIndex;
import com.jaoafa.jaotone.Framework.Command.CmdRouter;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import java.util.ArrayList;
import java.util.function.Consumer;

public class AppAnalysis {
    public static AppAnalysisResult analyzeAsApp(SlashCommandEvent event) {
        String cmdName = event.getName();
        String subCmdGroupName = event.getSubcommandGroup();
        String subCmdName = event.getSubcommandName();

        CmdRouter.CmdRoutingData routingData = CmdRouter.routeList.stream().filter(
                index ->
                        index.cmdName().equals(cmdName) &&
                                index.groupName().equals(subCmdGroupName) &&
                                index.subCmdName().equals(subCmdName)
        ).findFirst().orElse(null);


        if (routingData == null)
            return new AppAnalysisResult(null, null, ExecutionErrorType.CommandNotFound);

        ArrayList<CmdOptionIndex> optionIndexList = new ArrayList<>();

        for (OptionMapping option : event.getOptions()) {
            OptionData currentOptionData =
                    routingData.optionData().stream().filter(
                            optionData -> optionData.getName().equals(option.getName())
                    ).findFirst().orElse(null);

            if (currentOptionData == null)
                return new AppAnalysisResult(routingData, null, ExecutionErrorType.InvalidOptionName);

            Consumer<Object> addAs = object -> optionIndexList.add(new CmdOptionIndex(currentOptionData, object));

            switch (option.getType()) {
                case BOOLEAN -> addAs.accept(option.getAsBoolean());
                case STRING -> addAs.accept(option.getAsString());
                case INTEGER -> addAs.accept(option.getAsLong());
                case MENTIONABLE -> addAs.accept(option.getAsMentionable());
                case ROLE -> addAs.accept(option.getAsRole());
                case CHANNEL -> addAs.accept(option.getAsMessageChannel());
                case USER -> addAs.accept(option.getAsUser());
            }
        }
        return new AppAnalysisResult(routingData, optionIndexList, ExecutionErrorType.NoError);
    }

    public enum ExecutionErrorType {
        NoError, CommandNotFound, InvalidOptionName
    }

    public record AppAnalysisResult(CmdRouter.CmdRoutingData routingData,
                                    ArrayList<CmdOptionIndex> optionIndices,
                                    ExecutionErrorType errorType) {
    }
}
