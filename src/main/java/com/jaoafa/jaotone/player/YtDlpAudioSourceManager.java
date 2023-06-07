package com.jaoafa.jaotone.player;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManager;
import com.sedmelluq.discord.lavaplayer.track.AudioItem;
import com.sedmelluq.discord.lavaplayer.track.AudioReference;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackInfo;
import org.json.JSONObject;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.TimeUnit;

public class YtDlpAudioSourceManager implements AudioSourceManager {
    public Process process = null;

    @Override
    public String getSourceName() {
        return "yt-dlp";
    }

    @Override
    public AudioItem loadItem(AudioPlayerManager manager, AudioReference reference) {
        System.out.println("YtDlpAudioSourceManager: " + reference.identifier);
        // 動画情報を取得
        ProcessBuilder pb = new ProcessBuilder("yt-dlp", "-j", "--no-playlist", "--no-warnings", "--no-progress", "--no-continue", "--no-cache-dir", "--ignore-errors", reference.identifier);
        pb.redirectErrorStream(true);
        try {
            Process p = pb.start();
            p.waitFor(10, TimeUnit.SECONDS);
            if (p.exitValue() != 0) {
                System.out.println("exitValue: " + p.exitValue());
                System.out.println("output: " + new String(p.getInputStream().readAllBytes()));
                return null;
            }
            String raw = new String(p.getInputStream().readAllBytes());
            JSONObject json = new JSONObject(raw);

            String tempDir = System.getProperty("java.io.tmpdir");
            Path path = Paths.get(tempDir, json.getString("extractor"), json.getString("id") + ".mp4");

            return new YtDlpAudioTrack(new AudioTrackInfo(
                    json.getString("title"),
                    json.getString("uploader"),
                    json.getLong("duration"),
                    reference.identifier,
                    false,
                    json.getString("webpage_url")
            ), path, this);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean isTrackEncodable(AudioTrack track) {
        return true;
    }

    @Override
    public void encodeTrack(AudioTrack track, DataOutput output) {
        // Nothing special to encode
    }

    @Override
    public AudioTrack decodeTrack(AudioTrackInfo trackInfo, DataInput input) {
        return null;
    }

    @Override
    public void shutdown() {
        if (process != null) {
            process.destroy();
        }
    }
}
