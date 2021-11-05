package com.jaoafa.jaotone.Lib.jaoTone.Music;

import com.jaoafa.jaotone.Lib.jaoTone.LibValue;
import com.jayway.jsonpath.JsonPath;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Optional;
import java.util.function.Function;

public class LibVideo {
    public static SearchResult searchYouTube(String term, int limit) {
        String url = "https://www.googleapis.com/youtube/v3/search?type=video&part=snippet&q=%s&maxResults=%s&key=%s"
                .formatted(term, limit, LibValue.get("YouTube"));

        JSONArray items;
        try (Response response =
                     new OkHttpClient().newCall(
                             new Request.Builder().url(url).build()
                     ).execute()) {
            items = new JSONObject(
                    Optional.ofNullable(response.body()).orElseThrow().string()
            ).getJSONArray("items");
        } catch (NullPointerException | IOException | JSONException e) {
            return new SearchResult(null, ErrorType.NotFound);
        }

        ArrayList<LibVideoInfo> videoInfos = new ArrayList<>() {{
            for (Object item : items) {
                Function<String, String> getByPath = (path) -> JsonPath.read(item.toString(), path);
                add(new LibVideoInfo(
                        getByPath.apply("$.snippet.channelTitle"),
                        "https://youtube.com/channels/" + getByPath.apply("$.snippet.channelId"),
                        getByPath.apply("$.snippet.title"),
                        "https://youtu.be/" + getByPath.apply("$.id.videoId"),
                        getByPath.apply("$.snippet.description"),
                        getByPath.apply("$.snippet.thumbnails.default.url"),
                        getByPath.apply("$.snippet.thumbnails.medium.url"),
                        getByPath.apply("$.snippet.thumbnails.high.url"),
                        getByPath.apply("$.snippet.publishTime")
                ));
            }
        }};
        return new SearchResult(videoInfos, ErrorType.NoError);
    }

    public static SearchResult searchLocal(String path) {
        File targetFile = new File(path);
        if (!targetFile.exists())
            return new SearchResult(null, ErrorType.NotFound);

        String targetPath = targetFile.getPath();
        return new SearchResult(new ArrayList<>() {{
            add(new LibVideoInfo(
                    null,
                    null,
                    targetFile.getName(),
                    targetPath,
                    "ローカルパス:`%s` から再生".formatted(targetPath),
                    null,
                    null,
                    null,
                    null
            ));
        }}, ErrorType.NoError);
    }

    public static SearchResult searchHttp(String url) {
        try (Response ignored =
                     new OkHttpClient().newCall(
                             new Request.Builder().url(url).build()
                     ).execute()) {
            String[] urlPath = url.split("/");
            return new SearchResult(new ArrayList<>() {{
                add(new LibVideoInfo(
                        null,
                        null,
                        urlPath[urlPath.length - 1],
                        url,
                        "HTTP: %s から再生".formatted(url),
                        null,
                        null,
                        null,
                        null
                ));
            }}, ErrorType.NoError);
        } catch (NullPointerException | IOException | JSONException e) {
            return new SearchResult(null, ErrorType.NotFound);
        }
    }


    public record SearchResult(ArrayList<LibVideoInfo> videoInfos,
                               ErrorType errorType) {
    }

    public enum ErrorType {
        NotFound, NoError
    }
}
