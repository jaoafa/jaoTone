package com.jaoafa.jaotone.lib;

import org.json.JSONObject;

import java.nio.file.Files;
import java.nio.file.Path;

/**
 * jaoTone 設定管理クラス
 */
public class ToneConfig {
    JSONObject config;

    /**
     * {@link ToneConfig} クラスの新しいインスタンスを初期化します。
     * <p>
     * CONFIG_PATH 環境変数に値が設定されている場合、そのパスのファイルを読み込みます。<br>
     * 設定されていない場合、<code>config.json</code> を読み込みます。
     */
    public ToneConfig() {
        Path path = Path.of(System.getenv("CONFIG_PATH") != null ? System.getenv("CONFIG_PATH") : "config.json");
        if (!Files.exists(path)) {
            throw new RuntimeException("Config file not found.");
        }

        try {
            String json = Files.readString(path);
            config = new JSONObject(json);
        } catch (Exception e) {
            throw new RuntimeException("Config file read error.", e);
        }
    }

    /**
     * Discord トークンを取得します。
     *
     * @return Discord トークン
     */
    public String getToken() {
        return config.getString("token");
    }

    /**
     * Bot のオーナーとなるユーザー ID を取得します。
     *
     * @return Bot のオーナーとなるユーザー ID
     */
    public String getOwnerId() {
        return config.getString("ownerId");
    }

    /**
     * Bot のプレフィックスを取得します。指定されていない場合、<code>!</code> を返します。
     *
     * @return Bot のプレフィックス
     */
    public String getPrefix() {
        return config.optString("prefix", "!");
    }
}
