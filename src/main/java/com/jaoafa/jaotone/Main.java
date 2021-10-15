package com.jaoafa.jaotone;

import com.jagrosh.jdautilities.commons.waiter.EventWaiter;
import com.jaoafa.jaotone.Framework.Action.ActionRecorder;
import com.jaoafa.jaotone.Framework.Command.App.AppHooker;
import com.jaoafa.jaotone.Framework.Command.App.AppRecorder;
import com.jaoafa.jaotone.Framework.Command.Scope.ScopeRecorder;
import com.jaoafa.jaotone.Framework.Command.Text.TextHooker;
import com.jaoafa.jaotone.Framework.Event.EventRecorder;
import com.jaoafa.jaotone.Lib.jaoTone.LibValue;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.requests.GatewayIntent;

import java.io.File;

import static com.jaoafa.jaotone.Lib.Universal.LibFlow.Type.*;
import static com.jaoafa.jaotone.Lib.Universal.LibFlow.print;

public class Main {
    public static void main(String[] args) {
        print(Task, "起動中...");

        String configPath = args.length > 0 ? args[0] : "./config.json";
        if (!new File(configPath).exists()) {
            print(Failure, "設定ファイル %s が存在しません".formatted(configPath));
            return;
        } else {
            LibValue.CONFIG_PATH = configPath;
            print(Success, "設定ファイル %s を使用します".formatted(configPath));
        }

        LibValue.reload();
        EventWaiter eventWaiter = new EventWaiter();
        JDABuilder jdaBuilder =
                JDABuilder.createDefault(
                        LibValue.get("Discord"),
                        GatewayIntent.GUILD_PRESENCES,
                        GatewayIntent.GUILD_MESSAGES,
                        GatewayIntent.GUILD_MEMBERS,
                        GatewayIntent.GUILD_VOICE_STATES
                ).addEventListeners(eventWaiter, new AppHooker(), new TextHooker());

        new EventRecorder(jdaBuilder);

        try {
            LibValue.jda = jdaBuilder.build().awaitReady();
            LibValue.eventWaiter = eventWaiter;
            new ScopeRecorder();
            new AppRecorder(LibValue.jda);
            new ActionRecorder();
        } catch (Exception e) {
            print(Failure, "ログインに失敗しました。終了します。");
            e.printStackTrace();
            System.exit(1);
        }
    }
}
