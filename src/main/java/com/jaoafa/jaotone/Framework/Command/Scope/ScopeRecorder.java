package com.jaoafa.jaotone.Framework.Command.Scope;

import com.jaoafa.jaotone.Lib.Universal.LibClassFinder;
import com.jaoafa.jaotone.Lib.jaoTone.LibValue;

public class ScopeRecorder {
    public ScopeRecorder() throws Exception {
        for (Class<?> scopeClass :
                new LibClassFinder().findClasses("%s.Command".formatted(LibValue.ROOT_PACKAGE))
                                    .stream()
                                    .filter(cmdClass -> cmdClass.getSimpleName().equals("Scope"))
                                    .toList()) {
            PackedScope scope = ((ScopeSubstrate) scopeClass.getConstructor().newInstance()).scope();

            ScopeManager.scopes.put(scope.scope(), scope.scopeType(), scope.guilds());
        }
    }
}