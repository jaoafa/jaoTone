package com.jaoafa.jaotone.Framework.Command.Text;

import com.jaoafa.jaotone.Framework.Command.CmdOptionIndex;
import com.jaoafa.jaotone.Framework.Command.CmdRouter;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class TextAnalysis {
    public static TextAnalysisResult analyzeAsText(String text) {
        List<String> options = new LinkedList(Arrays.asList(text.split(" ")));
        ArrayList<CmdOptionIndex> optionIndexList = new ArrayList<>();

        List<CmdRouter.CmdRoutingData> filteredRoutingData = CmdRouter.routeList.stream().filter(
                index -> index.cmdName().equals(options.get(0).replace("^", ""))
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

        if (routingData.optionData().length > 0) {
            String isChannel = "<#[0-9]+>"; //Channel
            String isRole = "<@&[0-9]+>"; //Role
            String isBoolean = "[Tt]rue|[Ff]alse";
            String isMentionable = "<@(&??|!??)[0-9]+>|@everyone"; //Role or User or Everyone
            String isUser = "<@!??[0-9]+>"; //User

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

                        OptionData currentOptionData = Arrays.stream(routingData.optionData()).filter(
                                optionData -> optionData.getName().equals(optionName)
                        ).findFirst().orElse(null);

                        if (currentOptionData == null)
                            return new TextAnalysisResult(routingData, null, ExecutionErrorType.InvalidOptionName);

                        //todo 型チェック系functionとかにまとめる
                        boolean isValidOptionType = false;
                        switch (currentOptionData.getType()) {
                            //もともとがStringなので
                            case STRING -> isValidOptionType = true;
                            //パースしてみる
                            case INTEGER -> {
                                try {
                                    Integer.parseInt(optionIndex);
                                    isValidOptionType = true;
                                } catch (NumberFormatException ignored) {
                                }
                            }
                            //以下正規表現ゴリゴリ
                            case BOOLEAN -> isValidOptionType = optionIndex.matches(isBoolean);
                            case CHANNEL -> isValidOptionType = optionIndex.matches(isChannel);
                            case ROLE -> isValidOptionType = optionIndex.matches(isRole);
                            case MENTIONABLE -> isValidOptionType = optionIndex.matches(isMentionable);
                            case USER -> isValidOptionType = optionIndex.matches(isUser);
                        }

                        if (!isValidOptionType)
                            return new TextAnalysisResult(routingData, null, ExecutionErrorType.InvalidOptionType);

                        optionIndexList.add(new CmdOptionIndex(currentOptionData, optionIndex));
                    }
                    //全ての必須Optionが入力されているか確かめる
                    //fixme ユーザーが入力したやつも必須オプションだけにフィルターして比較しなきゃいけない
                    if (Arrays.stream(routingData.optionData()).filter(OptionData::isRequired).toList().size() > options.size())
                        return new TextAnalysisResult(routingData, null, ExecutionErrorType.NotEnoughOptions);
                }
                case OptionWithNoName -> {
                    int current = 0;
                    for (OptionData optionData : routingData.optionData()) {
                        boolean isValidOptionType = false;

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
                        switch (optionData.getType()) {
                            //もともとがStringなので
                            case STRING -> isValidOptionType = true;
                            //パースしてみる
                            case INTEGER -> {
                                try {
                                    Integer.parseInt(optionIndex);
                                    isValidOptionType = true;
                                } catch (NumberFormatException ignored) {
                                }
                            }
                            //以下正規表現ゴリゴリ
                            case BOOLEAN -> isValidOptionType = optionIndex.matches(isBoolean);
                            case CHANNEL -> isValidOptionType = optionIndex.matches(isChannel);
                            case ROLE -> isValidOptionType = optionIndex.matches(isRole);
                            case MENTIONABLE -> isValidOptionType = optionIndex.matches(isMentionable);
                            case USER -> isValidOptionType = optionIndex.matches(isUser);
                        }

                        if (!isValidOptionType)
                            return new TextAnalysisResult(routingData, null, ExecutionErrorType.InvalidOptionType);

                        optionIndexList.add(new CmdOptionIndex(optionData, optionIndex));
                        current++;
                    }
                }
            }
        }
        return new TextAnalysisResult(routingData, optionIndexList, ExecutionErrorType.NoError);
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
