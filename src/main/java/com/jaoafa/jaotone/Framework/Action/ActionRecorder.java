package com.jaoafa.jaotone.Framework.Action;

import com.jaoafa.jaotone.Lib.Universal.LibClassFinder;

public class ActionRecorder {
    public ActionRecorder() throws Exception {
        for (Class<?> action : new LibClassFinder().findClassesStartsWith("%s.Action", "Act_")) {
            ((ActionSubstrate) action.getConstructor().newInstance()).action().build();
        }
    }
}
