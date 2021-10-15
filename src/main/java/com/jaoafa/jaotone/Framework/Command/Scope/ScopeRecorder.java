package com.jaoafa.jaotone.Framework.Command.Scope;

import com.jaoafa.jaotone.Lib.Universal.LibClassFinder;
import com.jaoafa.jaotone.Lib.jaoTone.LibValue;

public class ScopeRecorder {
    /*
     * 1.Command以下のパッケージを取得
     * 2.それぞれのパッケージ以下にDefScope的なものを配置
     * 3.インターフェースとかかけて中身取り出してここのscopesに追加
     * ...出来たらわざわざここに書く必要ないしバグ減りそう
     * (Command登録時AppRecorderにDefScopeは除外するようフィルターかける)
     */
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
