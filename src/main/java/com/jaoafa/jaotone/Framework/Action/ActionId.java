package com.jaoafa.jaotone.Framework.Action;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.json.JSONObject;

public record ActionId(@NotNull String name, @Nullable String literal, @Nullable JSONObject data) {
    public String get() {
        JSONObject id = new JSONObject().put("name", name);
        if (literal != null) id.put("literal", literal);
        if (data != null) id.put("data", data);
        return id.toString();
    }
}
