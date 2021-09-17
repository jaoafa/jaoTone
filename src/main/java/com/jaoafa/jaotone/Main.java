package com.jaoafa.jaotone;

import com.jagrosh.jdautilities.commons.waiter.EventWaiter;
import com.jaoafa.jaotone.Framework.Command.App.AppHooker;
import com.jaoafa.jaotone.Framework.Command.App.AppRecorder;
import com.jaoafa.jaotone.Framework.Command.Text.TextHooker;
import com.jaoafa.jaotone.Framework.Event.EventRecorder;
import com.jaoafa.jaotone.Lib.jaoTone.LibValue;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.requests.GatewayIntent;

import static com.jaoafa.jaotone.Lib.Universal.LibFlow.Type.Failure;
import static com.jaoafa.jaotone.Lib.Universal.LibFlow.Type.Task;
import static com.jaoafa.jaotone.Lib.Universal.LibFlow.print;

public class Main {
    public static void main(String[] args) {
        print(Task, "起動中...");
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
            new AppRecorder(LibValue.jda);
        } catch (Exception e) {
            print(Failure, "ログインに失敗しました。終了します。");
            e.printStackTrace();
            System.exit(1);
        }
    }
}
