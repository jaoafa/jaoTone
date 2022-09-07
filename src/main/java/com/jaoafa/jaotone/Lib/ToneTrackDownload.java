package com.jaoafa.jaotone.lib;

import org.json.JSONObject;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.Set;

public class ToneTrackDownload {
    private static final Path directory;
    private static final Set<String> downloadingVideoIds = new HashSet<>();
    private static final Set<String> failedVideoIds = new HashSet<>();

    static {
        directory = System.getenv("JAOTONE_TRACK_DOWNLOAD_DIR") != null ?
                    Path.of(System.getenv("JAOTONE_TRACK_DOWNLOAD_DIR")) :
                    Path.of("data", "track-downloaded");
    }

    public static Path getServiceDirectory(ToneTrackService service) {
        return directory.resolve(service.name());
    }

    public static boolean isDownloaded(ToneTrackService service, String videoId) {
        Path downloadedFile = getServiceDirectory(service).resolve("downloaded.json");
        if (!Files.exists(downloadedFile)) {
            return false;
        }
        try {
            String json = Files.readString(downloadedFile);
            JSONObject jsonObject = new JSONObject(json);
            return jsonObject.has(videoId);
        } catch (IOException e) {
            return false;
        }
    }

    public static Path getDownloadedPath(ToneTrackService service, String videoId) {
        Path downloadedFile = getServiceDirectory(service).resolve("downloaded.json");
        if (!Files.exists(downloadedFile)) {
            return null;
        }
        try {
            String json = Files.readString(downloadedFile);
            JSONObject jsonObject = new JSONObject(json);
            return Path.of(jsonObject.getString(videoId));

        } catch (IOException e) {
            return null;
        }
    }

    public static void setDownloaded(ToneTrackService service, String videoId, Path path) {
        Path downloadedFile = getServiceDirectory(service).resolve("downloaded.json");
        try {
            JSONObject object = new JSONObject();
            if (Files.exists(downloadedFile)) {
                object = new JSONObject(Files.readString(downloadedFile));
            }
            object.put(videoId, path.toAbsolutePath().toString());
            if (!Files.exists(directory.getParent())) {
                Files.createDirectories(directory.getParent());
            }
            Files.writeString(downloadedFile, object.toString());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static boolean isDownloading(ToneTrackService service, String videoId) {
        return downloadingVideoIds.contains(service.name() + "-" + videoId) && !failedVideoIds.contains(service.name() + "-" + videoId);
    }

    public static void addDownloading(ToneTrackService service, String videoId) {
        downloadingVideoIds.add(service.name() + "-" + videoId);
    }

    public static void removeDownloading(ToneTrackService service, String videoId) {
        downloadingVideoIds.remove(service.name() + "-" + videoId);
    }

    public static boolean isFailed(ToneTrackService service, String videoId) {
        return failedVideoIds.contains(service.name() + "-" + videoId);
    }

    public static void setFailed(ToneTrackService service, String videoId) {
        failedVideoIds.add(service.name() + "-" + videoId);
    }
}
