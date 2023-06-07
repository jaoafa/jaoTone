package com.jaoafa.jaotone.player;

import com.sedmelluq.discord.lavaplayer.container.mpeg.MpegAudioTrack;
import com.sedmelluq.discord.lavaplayer.source.local.LocalSeekableInputStream;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackInfo;
import com.sedmelluq.discord.lavaplayer.track.DelegatedAudioTrack;
import com.sedmelluq.discord.lavaplayer.track.playback.LocalAudioTrackExecutor;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.concurrent.TimeUnit;

public class YtDlpAudioTrack extends DelegatedAudioTrack {
    private final Path path;
    private final YtDlpAudioSourceManager sourceManager;

    public YtDlpAudioTrack(AudioTrackInfo trackInfo, Path path, YtDlpAudioSourceManager sourceManager) {
        super(trackInfo);

        this.path = path;
        this.sourceManager = sourceManager;
    }

    @Override
    public void process(LocalAudioTrackExecutor executor) throws Exception {
        boolean result = downloadFile(path);
        if (!result) {
            throw new IOException("Failed to download file.");
        }

        File file = path.toFile();

        try (LocalSeekableInputStream inputStream = new LocalSeekableInputStream(file)) {
            processDelegate(new MpegAudioTrack(trackInfo, inputStream), executor);
        }
    }

    private boolean downloadFile(Path path) {
        File file = path.toFile();
        if (file.exists()) {
            return true;
        }

        if (!file.getParentFile().exists()) {
            if (!file.getParentFile().mkdir()) {
                return false;
            }
        }

        ProcessBuilder pb = new ProcessBuilder("yt-dlp", "-o", file.getAbsolutePath(), "--no-playlist", "--no-warnings", "--newline", "--no-continue", "--no-cache-dir", "--ignore-errors", trackInfo.identifier);
        pb.redirectErrorStream(true);
        Process p = null;
        try {
            pb.redirectOutput(ProcessBuilder.Redirect.PIPE);
            p = pb.start();

            this.sourceManager.process = p;

            boolean result = p.waitFor(3, TimeUnit.MINUTES);
            if (!result) {
                p.destroy();
                return false;
            }
            return p.exitValue() == 0;
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            if (p != null && p.isAlive()) {
                p.destroy();
            }
        }
        return false;
    }
}
