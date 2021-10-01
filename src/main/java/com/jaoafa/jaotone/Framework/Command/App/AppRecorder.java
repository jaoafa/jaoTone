package com.jaoafa.jaotone.Framework.Command.App;

import com.jaoafa.jaotone.Framework.Command.Builder.BuildCmd;
import com.jaoafa.jaotone.Framework.Command.CmdScope;
import com.jaoafa.jaotone.Framework.Command.CmdSubstrate;
import com.jaoafa.jaotone.Lib.Universal.LibClassFinder;
import com.jaoafa.jaotone.Main;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.jaoafa.jaotone.Lib.Universal.LibFlow.Type.*;
import static com.jaoafa.jaotone.Lib.Universal.LibFlow.print;

public class AppRecorder {
    public AppRecorder(JDA jda) throws Exception {
        print(Task, "SlashCommandを登録中...");

        String ROOT_PACKAGE = Main.class.getPackage().getName();
        List<Guild> guilds = jda.getGuilds();

        //GuildId / CommandDataArray
        Map<String, ArrayList<CommandData>> commandQueue = new HashMap<>();

        ArrayList<CommandData> publicCommands =
                new ArrayList<>(
                        checkCmd(new LibClassFinder().findClasses(
                                "%s.Command.Public".formatted(ROOT_PACKAGE)
                        ))
                );

        for (Guild guild : guilds) commandQueue.put(guild.getId(), new ArrayList<>(publicCommands));

        for (Map.Entry<String, String> entry : CmdScope.scopes.entrySet()) {
            String scopeName = entry.getKey();
            ArrayList<CommandData> commandDataList =
                    checkCmd(new LibClassFinder().findClasses(
                            "%s.Command.%s".formatted(ROOT_PACKAGE, scopeName)
                    ));

            for (CommandData commandData : commandDataList) {
                CmdScope.scopeList.put(commandData.getName(), scopeName);
                System.out.println("%s:%s".formatted(commandData.getName(), scopeName));
            }


            commandQueue.get(entry.getValue()).addAll(commandDataList);
        }


        for (Guild guild : guilds)
            guild.updateCommands().addCommands(commandQueue.get(guild.getId())).queue(
                    recordedCmd -> print(Success, "%s に %s コマンドを登録しました".formatted(guild.getName(), recordedCmd.size()))
            );
    }

    /**
     * クラスからコマンドデータを抽出します
     *
     * @param classes チェックするクラス
     * @return チェックを通過したクラスのコマンドデータ
     * @throws Exception コンストラクタ又はインスタンスの生成に失敗した場合
     */
    private static ArrayList<CommandData> checkCmd(List<Class<?>> classes) throws Exception {
        ArrayList<CommandData> result = new ArrayList<>();
        for (Class<?> cmd : classes) {
            if (!cmd.getSimpleName().startsWith("Cmd_")
                    || cmd.getEnclosingClass() != null
                    || cmd.getName().contains("$")) {
                print(Failure, "%s はCommandクラスではありません。スキップします...".formatted(cmd.getSimpleName()));
                continue;
            }

            BuildCmd checkedCmd = ((CmdSubstrate) cmd.getConstructor().newInstance()).builder().finalBuild();
            result.add(checkedCmd.commandData);
            print(Success, "%s のチェックが完了しました".formatted(checkedCmd.name));
        }
        return result;
    }
}