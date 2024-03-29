package com.jaoafa.jaotone;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandClient;
import com.jagrosh.jdautilities.command.CommandClientBuilder;
import com.jagrosh.jdautilities.commons.waiter.EventWaiter;
import com.jaoafa.jaotone.lib.ToneConfig;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.requests.GatewayIntent;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * jaoTone メインクラス
 */
public class Main {
    private static final Logger logger = LoggerFactory.getLogger("jaoTone");
    private static ToneConfig config;

    /**
     * メインメソッド
     *
     * @param args コマンドライン引数
     */
    public static void main(String[] args) {
        logger.info("jaoTone を起動しています…");

        config = new ToneConfig();
        EventWaiter waiter = new EventWaiter();
        CommandClient client = getCommandClient(config.getPrefix());

        // ログイン
        try {
            JDABuilder jdabuilder = JDABuilder
                    .createDefault(config.getToken())
                    .enableIntents(GatewayIntent.GUILD_MEMBERS,
                            GatewayIntent.GUILD_PRESENCES,
                            GatewayIntent.MESSAGE_CONTENT,
                            GatewayIntent.GUILD_MESSAGE_REACTIONS)
                    .setAutoReconnect(true)
                    .setBulkDeleteSplittingEnabled(false)
                    .setContextEnabled(false);

            jdabuilder.addEventListeners(waiter, client);
            registerEvent(jdabuilder);

            jdabuilder.build().awaitReady();
        } catch (Exception e) {
            logger.error("Discord へのログインに失敗", e);
            return;
        }

        logger.info("jaoTone を起動しました。");
    }

    static CommandClient getCommandClient(String prefix) {
        CommandClientBuilder builder = new CommandClientBuilder();

        builder.setPrefix(prefix);
        builder.setActivity(null);
        builder.setOwnerId(config.getOwnerId());
        builder.setEmojis("✅", "⚠️", "❌");

        registerCommand(builder);

        return builder.build();
    }

    static void registerCommand(CommandClientBuilder builder) {
        final String commandPackage = "com.jaoafa.jaotone.command";
        Reflections reflections = new Reflections(commandPackage);
        Set<Class<? extends Command>> subTypes = reflections.getSubTypesOf(Command.class);
        List<Command> commands = new ArrayList<>();

        for (Class<? extends Command> theClass : subTypes) {
            if (!theClass.getName().startsWith("%s.Cmd_".formatted(commandPackage))) {
                continue;
            }
            if (theClass.getName().contains("SubCommand") || theClass.getName().contains("SlashCommand")) {
                continue;
            }
            String cmdName = theClass.getName().substring(("%s.Cmd_".formatted(commandPackage)).length());

            try {
                commands.add(theClass.getDeclaredConstructor().newInstance());
                logger.info("%s: コマンドの登録に成功しました".formatted(cmdName));
            } catch (Throwable throwable) {
                logger.error("%s: コマンドの登録に失敗しました".formatted(cmdName), throwable);
            }
        }
        builder.addCommands(commands.toArray(new Command[0]));
    }

    static void registerEvent(JDABuilder jdaBuilder) {
        final String eventPackage = "com.jaoafa.jaotone.event";
        Reflections reflections = new Reflections(eventPackage);
        Set<Class<? extends ListenerAdapter>> subTypes = reflections.getSubTypesOf(ListenerAdapter.class);
        for (Class<? extends ListenerAdapter> clazz : subTypes) {
            if (!clazz.getName().startsWith("%s.Event_".formatted(eventPackage))) {
                continue;
            }
            if (clazz.getEnclosingClass() != null) {
                continue;
            }
            if (clazz.getName().contains("$")) {
                continue;
            }
            String eventName = clazz.getName().substring(("%s.Event_".formatted(eventPackage)).length());
            try {
                Constructor<?> construct = clazz.getConstructor();
                Object instance = construct.newInstance();
                if (!(instance instanceof ListenerAdapter)) {
                    return;
                }

                jdaBuilder.addEventListeners(instance);
                logger.info("%s: イベントの登録に成功しました。".formatted(eventName));
            } catch (NoSuchMethodException | InstantiationException | IllegalAccessException |
                     InvocationTargetException e) {
                logger.error("%s: イベントの登録に失敗しました。".formatted(eventName), e);
            }
        }
    }

    /**
     * jaoTone のロガーを取得します。
     *
     * @return jaoTone のロガー
     */
    public static Logger getLogger() {
        return logger;
    }
}
