package com.jaoafa.jaotone.libtemp;

import org.json.JSONObject;

import java.nio.file.Files;
import java.nio.file.Path;

public class ToneConfig {
    JSONObject config;

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

    public String getToken() {
        return config.getString("token");
    }

    public String getOwnerId() {
        return config.getString("ownerId");
    }
}
