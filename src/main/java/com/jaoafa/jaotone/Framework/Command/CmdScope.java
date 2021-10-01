package com.jaoafa.jaotone.Framework.Command;

import java.util.HashMap;
import java.util.Map;

public class CmdScope {
    /*
     * 1.Command以下のパッケージを取得
     * 2.それぞれのパッケージ以下にDefScope的なものを配置
     * 3.インターフェースとかかけて中身取り出してここのscopesに追加
     * ...出来たらわざわざここに書く必要ないしバグ減りそう
     * (Command登録時AppRecorderにDefScopeは除外するようフィルターかける)
     */
    //ScopeName / GuildID
    public static Map<String, String> scopes = new HashMap<>() {{
        put("jaoafa", "597378876556967936");
        put("jaodev", "840578119467401257");
    }};

    //CmdName / ScopeName
    public static Map<String, String> scopeList = new HashMap<>();
}
