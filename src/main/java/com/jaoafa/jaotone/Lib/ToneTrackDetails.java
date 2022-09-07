package com.jaoafa.jaotone.lib;

import net.dv8tion.jda.api.entities.Message;
import org.json.JSONObject;

import javax.annotation.CheckReturnValue;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class ToneTrackDetails {
    private final Path directory;
    private final ToneTrackService service;
    private final String videoId;
    private boolean isLoaded;

    private String title;
    private String description;
    private String thumbnailUrl;
    private String author;

    private ToneTrackDetails(ToneTrackService service, String videoId) {
        this.directory = System.getenv("JAOTONE_TRACK_DETAILS_DIR") != null ?
                         Path.of(System.getenv("JAOTONE_TRACK_DETAILS_DIR")) :
                         Path.of("data", "track-details");
        if (!Files.exists(directory)) {
            try {
                Files.createDirectories(directory);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        this.service = service;
        this.videoId = videoId;

        try {
            load();
            this.isLoaded = true;
        } catch (IOException e) {
            this.isLoaded = false;
        }
    }

    /**
     * ToneTrackDetails インスタンスを生成します。
     *
     * @param track 対象のトラック
     * @return ToneTrackDetails インスタンス
     */
    @CheckReturnValue
    public static ToneTrackDetails of(ToneTrack track) {
        return new ToneTrackDetails(track.service(), track.videoId());
    }

    /**
     * ToneTrackDetails インスタンスを生成します。
     *
     * @param service トラックのサービス
     * @param videoId トラックの ID
     * @return ToneTrackDetails インスタンス
     */
    @CheckReturnValue
    public static ToneTrackDetails of(ToneTrackService service, String videoId) {
        return new ToneTrackDetails(service, videoId);
    }

    @CheckReturnValue
    public ToneTrackDetails title(String title) {
        this.title = title;
        return this;
    }

    @CheckReturnValue
    public ToneTrackDetails description(String description) {
        this.description = description;
        return this;
    }

    @CheckReturnValue
    public ToneTrackDetails thumbnailUrl(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
        return this;
    }

    @CheckReturnValue
    public ToneTrackDetails author(String author) {
        this.author = author;
        return this;
    }

    public ToneTrackDetails save() throws IOException {
        Path file = directory.resolve(Path.of(service.name(), videoId + ".json"));
        if (file.getParent() != null && !Files.exists(file.getParent())) {
            Files.createDirectories(file.getParent());
        }
        Files.writeString(file, toJSON().toString());
        return this;
    }

    @SuppressWarnings("UnusedReturnValue")
    public ToneTrackDetails updateAddVideoMessage(Message message) {
        if (message == null) {
            return this;
        }
        message.editMessage(":white_check_mark: `%s` (`%s`) を再生キューに追加しました。".formatted(title, author)).queue();
        return this;
    }

    public void load() throws IOException {
        Path file = directory.resolve(Path.of(service.name(), videoId + ".json"));
        if (!Files.exists(file)) {
            throw new IOException("File not found: " + file);
        }
        JSONObject json = new JSONObject(Files.readString(file));
        long datetime = json.getLong("datetime");
        // 1時間キャッシュ
        if (System.currentTimeMillis() - datetime > 1000 * 60 * 60) {
            throw new IOException("Cache expired: " + file);
        }
        JSONObject data = json.getJSONObject("data");
        this.title = data.getString("title");
        this.description = data.getString("description");
        this.thumbnailUrl = data.getString("thumbnailUrl");
        this.author = data.getString("author");
    }

    public JSONObject toJSON() {
        JSONObject json = new JSONObject();
        json.put("data", new JSONObject()
            .put("title", title)
            .put("description", description)
            .put("thumbnailUrl", thumbnailUrl)
            .put("author", author));
        json.put("datetime", System.currentTimeMillis());
        return json;
    }

    // ----------- Getter ----------- //

    public ToneTrackService getService() {
        return service;
    }

    public String getVideoId() {
        return videoId;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public String getAuthor() {
        return author;
    }

    public boolean isLoaded() {
        return isLoaded;
    }
}
