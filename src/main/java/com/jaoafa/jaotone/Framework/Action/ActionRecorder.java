package com.jaoafa.jaotone.Framework.Action;

import com.jaoafa.jaotone.Lib.Universal.LibClassFinder;
import com.jaoafa.jaotone.Lib.jaoTone.LibValue;

public class ActionRecorder {
    public ActionRecorder() throws Exception {
        for (Class<?> action : new LibClassFinder().findClassesStartsWith("%s.Action".formatted(LibValue.ROOT_PACKAGE), "Act_")) {
            ((ActionSubstrate) action.getConstructor().newInstance()).action();
        }
    }
}
