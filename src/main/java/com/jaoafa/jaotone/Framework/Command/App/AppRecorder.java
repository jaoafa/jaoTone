package com.jaoafa.jaotone.Framework.Command.App;

import com.jaoafa.jaotone.Framework.Command.Builder.BuildCmd;
import com.jaoafa.jaotone.Framework.Command.CmdSubstrate;
import com.jaoafa.jaotone.Lib.Universal.LibClassFinder;
import com.jaoafa.jaotone.Main;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;

import java.util.ArrayList;

import static com.jaoafa.jaotone.Lib.Universal.LibFlow.Type.*;
import static com.jaoafa.jaotone.Lib.Universal.LibFlow.print;

public class AppRecorder {
    public AppRecorder(JDA jda) throws Exception {
        print(Task, "SlashCommandを登録中...");

        ArrayList<CommandData> commands = new ArrayList<>();
        for (Class<?> cmdClass : new LibClassFinder().findClasses(Main.class.getPackage().getName() + ".Command")) {
            if (!cmdClass.getSimpleName().startsWith("Cmd_")
                    || cmdClass.getEnclosingClass() != null
                    || cmdClass.getName().contains("$")) {
                print(Failure, "%s はCommandクラスではありません。スキップします...".formatted(cmdClass.getSimpleName()));
                continue;
            }

            CmdSubstrate cmdInstance = ((CmdSubstrate) cmdClass.getConstructor().newInstance());
            BuildCmd buildCmd = cmdInstance.builder();
            commands.add(buildCmd.commandData);
            print(Success, "%s を登録キューに挿入しました".formatted(buildCmd.name));
        }
        for (Guild guild : jda.getGuilds()) {
            guild.updateCommands().addCommands(commands).queue(
                    recordedCmd -> print(Success, "%s に %s コマンドを登録しました".formatted(guild.getName(), recordedCmd.size()))
            );
        }
    }
}
