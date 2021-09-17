package com.jaoafa.jaotone.Framework.Event;

import com.jaoafa.jaotone.Lib.Universal.LibClassFinder;
import com.jaoafa.jaotone.Main;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.lang.reflect.Constructor;

import static com.jaoafa.jaotone.Lib.Universal.LibFlow.Type.Failure;
import static com.jaoafa.jaotone.Lib.Universal.LibFlow.Type.Success;
import static com.jaoafa.jaotone.Lib.Universal.LibFlow.print;

public class EventRecorder {
    public EventRecorder(JDABuilder jdaBuilder) {
        try {
            for (Class<?> eventClass : new LibClassFinder().findClasses(Main.class.getPackage().getName() + ".Event")) {
                String eventClassName = eventClass.getSimpleName();

                if (!eventClassName.startsWith("Event_")
                        || eventClass.getEnclosingClass() != null
                        || eventClass.getName().contains("$")) {
                    print(Failure, "%s はEventクラスではありません。スキップします...".formatted(eventClassName));
                    continue;
                }

                Object instance = ((Constructor<?>) eventClass.getConstructor()).newInstance();
                if (instance instanceof ListenerAdapter) {
                    jdaBuilder.addEventListeners(instance);
                    print(Success, "%sを登録しました。".formatted(eventClassName));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
