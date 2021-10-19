package com.jaoafa.jaotone.Framework.Command.App;

import com.google.common.collect.Table;
import com.jaoafa.jaotone.Framework.Command.Builder.PackedCmd;
import com.jaoafa.jaotone.Framework.Command.CmdSubstrate;
import com.jaoafa.jaotone.Framework.Command.Scope.ScopeManager;
import com.jaoafa.jaotone.Framework.Command.Scope.ScopeType;
import com.jaoafa.jaotone.Lib.Universal.LibClassFinder;
import com.jaoafa.jaotone.Lib.jaoTone.LibValue;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import org.jetbrains.annotations.NotNull;

import java.util.*;

import static com.jaoafa.jaotone.Lib.Universal.LibFlow.Type.Success;
import static com.jaoafa.jaotone.Lib.Universal.LibFlow.Type.Task;
import static com.jaoafa.jaotone.Lib.Universal.LibFlow.print;

public class AppRecorder {
    public AppRecorder(JDA jda) throws Exception {
        print(Task, "SlashCommandを登録中...");

        List<Guild> guilds = jda.getGuilds();

        //GuildId / CommandDataArray
        Map<String, ArrayList<CommandData>> commandQueue = new HashMap<>() {{
            for (Guild guild : guilds) put(guild.getId(), new ArrayList<>());
        }};

        for (Table.Cell<@NotNull String, @NotNull ScopeType, @NotNull ArrayList<String>> entry : ScopeManager.scopes.cellSet()) {
            String scopeName = entry.getRowKey();
            ArrayList<CommandData> commandDataList =
                    checkCmd("%s.Command.%s".formatted(LibValue.ROOT_PACKAGE, scopeName));

            for (CommandData commandData : commandDataList)
                ScopeManager.scopeList.put(commandData.getName(), scopeName);

            switch (Objects.requireNonNull(entry.getColumnKey())) {
                case Public -> {
                    for (Guild guild : guilds)
                        commandQueue.get(guild.getId()).addAll(commandDataList);
                }
                case Private -> {
                    for (String guildId : Objects.requireNonNull(entry.getValue()))
                        commandQueue.get(guildId).addAll(commandDataList);
                }
            }
        }

        for (Guild guild : guilds)
            guild.updateCommands().addCommands(commandQueue.get(guild.getId())).queue(
                    recordedCmd -> print(Success, "%s に %s コマンドを登録しました".formatted(guild.getName(), recordedCmd.size()))
            );
    }

    /**
     * クラスからコマンドデータを抽出します
     *
     * @return チェックを通過したクラスのコマンドデータ
     * @throws Exception コンストラクタ又はインスタンスの生成に失敗した場合
     */
    private static ArrayList<CommandData> checkCmd(String root) throws Exception {
        ArrayList<CommandData> result = new ArrayList<>();
        for (Class<?> cmd : new LibClassFinder()
                .findClassesStartsWith(root, "Cmd_")
                .stream()
                .filter(cmdClass -> !cmdClass.getSimpleName().equals("Scope"))
                .toList()) {
            PackedCmd checkedCmd = ((CmdSubstrate) cmd.getConstructor().newInstance()).command();
            result.add(checkedCmd.commandData());
        }
        return result;
    }
}