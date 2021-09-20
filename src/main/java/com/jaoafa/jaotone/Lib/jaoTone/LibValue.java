package com.jaoafa.jaotone.Lib.jaoTone;

import com.jagrosh.jdautilities.commons.waiter.EventWaiter;
import com.jaoafa.jaotone.Lib.Universal.LibFile;
import net.dv8tion.jda.api.JDA;
import org.apache.commons.codec.digest.DigestUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class LibValue {
    private static final Map<String, String> valueString = new HashMap<>();
    private static final Map<String, Integer> valueInteger = new HashMap<>();
    private static final Map<String, ArrayList<Object>> valueArray = new HashMap<>();
    public static Map<String, String> session = new HashMap<>();
    public static Map<String, String> cmdAlias = new HashMap<>();
    public static JDA jda = null;
    public static EventWaiter eventWaiter = null;
    public static String CONFIG_PATH;
    private static String configLastHash = "";
    private static JSONObject configJSON = null;

    public static void reload() {
        if (!configLastHash.equals(DigestUtils.md5Hex(LibFile.read(CONFIG_PATH)))) {
            configLastHash = DigestUtils.md5Hex(LibFile.read(CONFIG_PATH));
            configJSON = new JSONObject(LibFile.read(CONFIG_PATH));
        }

        analyzeConfig(configJSON);
    }

    private static void analyzeConfig(JSONObject jsonObject) {
        for (String key : jsonObject.keySet()) {
            Object currentObject = jsonObject.get(key);

            if (currentObject instanceof String)
                valueString.put(key, (String) currentObject);

            if (currentObject instanceof Integer)
                valueInteger.put(key, (Integer) currentObject);

            if (currentObject instanceof JSONObject)
                analyzeConfig((JSONObject) currentObject);

            if (currentObject instanceof JSONArray) {
                ArrayList<Object> arrayList = new ArrayList<>();

                for (Object object : ((JSONArray) currentObject))
                    arrayList.add(object);

                valueArray.put(key, arrayList);
            }
        }
    }

    public static String get(String key) {
        return Optional.ofNullable(valueString.get(key)).orElse(null);
    }

    public static Integer getInt(String key) {
        return valueInteger.get(key);
    }

    public static ArrayList<Object> getArray(String key) {
        return valueArray.get(key);
    }
}
