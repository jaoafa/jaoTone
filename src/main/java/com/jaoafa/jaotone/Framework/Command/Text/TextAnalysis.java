package com.jaoafa.jaotone.Framework.Command.Text;

import com.jaoafa.jaotone.Framework.Command.CmdOptionIndex;
import com.jaoafa.jaotone.Framework.Command.CmdRouter;
import com.jaoafa.jaotone.Framework.Command.CmdScope;
import com.jaoafa.jaotone.Lib.Discord.LibPrefix;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import java.util.*;

public class TextAnalysis {
    static String isChannel = "<#[0-9]+>"; //Channel
    static String isRole = "<@&[0-9]+>"; //Role
    static String isBoolean = "[Tt]rue|[Ff]alse";
    static String isMentionable = "<@(&??|!??)[0-9]+>|@everyone"; //Role or User or Everyone
    static String isUser = "<@!??[0-9]+>"; //User

    public static TextAnalysisResult analyzeAsText(String guildId, String text) {
        LinkedList<String> options = new LinkedList<>(Arrays.asList(text.split(" ")));
        ArrayList<CmdOptionIndex> optionIndexList = new ArrayList<>();

        List<CmdRouter.CmdRoutingData> filteredRoutingData = CmdRouter.routeList.stream().filter(
                index -> index.cmdName().equals(options.get(0).substring(LibPrefix.getPrefix(guildId).length()))
        ).toList();

        CmdOptionLengthType cmdOptionLengthType = null;
        if (filteredRoutingData.stream().anyMatch(index -> index.groupName() == null && index.subCmdName() == null))
            cmdOptionLengthType = CmdOptionLengthType.CMD;
        if (filteredRoutingData.stream().anyMatch(index -> index.groupName() == null && index.subCmdName() != null))
            cmdOptionLengthType = CmdOptionLengthType.CMD_SUBCMD;
        if (filteredRoutingData.stream().anyMatch(index -> index.groupName() != null && index.subCmdName() != null))
            cmdOptionLengthType = CmdOptionLengthType.CMD_GROUP_SUBCMD;

        if (cmdOptionLengthType == null)
            return new TextAnalysisResult(null, null, ExecutionErrorType.CommandNotFound);


        CmdRouter.CmdRoutingData routingData = null;

        switch (cmdOptionLengthType) {
            case CMD -> {
                routingData = filteredRoutingData.stream().findFirst().orElse(null);
                //コマンド分削除
                options.remove(0);
            }
            case CMD_SUBCMD -> {
                routingData = filteredRoutingData.stream().filter(index ->
                        index.subCmdName().equals(options.get(1))
                ).findFirst().orElse(null);
                //コマンド+サブコマンド分削除
                options.remove(0);
                options.remove(0);
            }
            case CMD_GROUP_SUBCMD -> {
                routingData = filteredRoutingData.stream().filter(index ->
                        index.groupName().equals(options.get(1)) && index.subCmdName().equals(options.get(2))
                ).findFirst().orElse(null);
                //コマンド+グループ+サブコマンド分削除

                options.remove(0);
                options.remove(0);
                options.remove(0);
            }
        }

        if (routingData == null)
            return new TextAnalysisResult(null, null, ExecutionErrorType.CommandNotFound);

        if (routingData.optionData().size() > 0) {
            String isOptionWithName = ".+:.+";

            boolean isMixedOptionForm = false;
            OptionForm optionForm = options.get(0).matches(isOptionWithName) ? OptionForm.OptionWithName : OptionForm.OptionWithNoName;
            for (String option : options) {
                if (!isMixedOptionForm) isMixedOptionForm =
                        !optionForm.equals(
                                option.matches(isOptionWithName) ?
                                        OptionForm.OptionWithName :
                                        OptionForm.OptionWithNoName
                        );
            }
            if (isMixedOptionForm)
                return new TextAnalysisResult(routingData, null, ExecutionErrorType.MixedOptionForm);


            switch (optionForm) {
                case OptionWithName -> {
                    for (String option : options) {
                        String[] optionNameAndIndex = option.split(":");
                        String optionName = optionNameAndIndex[0];
                        String optionIndex = optionNameAndIndex[1];

                        OptionData optionData = routingData.optionData().stream().filter(
                                data -> data.getName().equals(optionName)
                        ).findFirst().orElse(null);

                        if (optionData == null)
                            return new TextAnalysisResult(routingData, null, ExecutionErrorType.InvalidOptionName);

                        if (checkIsInvalidOptionType(optionData, optionIndex))
                            return new TextAnalysisResult(routingData, null, ExecutionErrorType.InvalidOptionType);

                        optionIndexList.add(new CmdOptionIndex(optionData, optionIndex));
                    }

                    if (routingData.optionData().stream().filter(OptionData::isRequired).toList().size() >
                            optionIndexList.stream().filter(cmdOptionIndex -> cmdOptionIndex.optionData().isRequired()).toList().size())
                        return new TextAnalysisResult(routingData, null, ExecutionErrorType.NotEnoughOptions);
                }
                case OptionWithNoName -> {
                    int current = 0;
                    for (OptionData optionData : routingData.optionData()) {

                        String optionIndex;
                        try {
                            optionIndex = options.get(current);
                        } catch (IndexOutOfBoundsException ignored) {
                            if (optionData.isRequired()) {
                                return new TextAnalysisResult(routingData, null, ExecutionErrorType.NotEnoughOptions);
                            } else {
                                continue;
                            }
                        }

                        if (checkIsInvalidOptionType(optionData, optionIndex))
                            return new TextAnalysisResult(routingData, null, ExecutionErrorType.InvalidOptionType);

                        optionIndexList.add(new CmdOptionIndex(optionData, optionIndex));
                        current++;
                    }
                }
            }
        }
        Map<String, String> scopeList = CmdScope.scopeList;
        //Scopeが有効であり、GuildIDが一致しない場合
        if (scopeList.containsKey(routingData.cmdName()) && !scopeList.get(scopeList.get(routingData.cmdName())).equals(guildId))
            return new TextAnalysisResult(routingData, optionIndexList, ExecutionErrorType.CommandNotFound);

        return new TextAnalysisResult(routingData, optionIndexList, ExecutionErrorType.NoError);
    }

    private static boolean checkIsInvalidOptionType(OptionData optionData, String optionIndex) {
        return !switch (optionData.getType()) {
            //もともとがStringなので
            case STRING -> true;
            //パースしてみる
            case INTEGER -> {
                try {
                    Integer.parseInt(optionIndex);
                    yield true;
                } catch (NumberFormatException ignored) {
                    yield false;
                }
            }
            //以下正規表現ゴリゴリ
            case BOOLEAN -> optionIndex.matches(isBoolean);
            case CHANNEL -> optionIndex.matches(isChannel);
            case ROLE -> optionIndex.matches(isRole);
            case MENTIONABLE -> optionIndex.matches(isMentionable);
            case USER -> optionIndex.matches(isUser);
            default -> false;
        };
    }

    public enum ExecutionErrorType {
        NoError, InvalidOptionType, NotEnoughOptions, CommandNotFound, MixedOptionForm, InvalidOptionName
    }

    public enum OptionForm {
        OptionWithName, OptionWithNoName
    }

    private enum CmdOptionLengthType {
        CMD, CMD_SUBCMD, CMD_GROUP_SUBCMD
    }

    public record TextAnalysisResult(CmdRouter.CmdRoutingData routingData, ArrayList<CmdOptionIndex> optionIndices,
                                     ExecutionErrorType errorType) {
    }
}
