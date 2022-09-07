package com.jaoafa.jaotone.player;


import com.jaoafa.jaotone.Main;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventAdapter;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackEndReason;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class TrackScheduler extends AudioEventAdapter {
    private final AudioPlayer player;
    private final BlockingQueue<AudioTrack> queue;
    private RepeatMode repeatMode = RepeatMode.DISABLE;

    public TrackScheduler(AudioPlayer player) {
        this.player = player;
        this.queue = new LinkedBlockingQueue<>();
    }

    public void queue(AudioTrack track) {
        if (player.startTrack(track, true)) {
            return;
        }
        if (!queue.offer(track)) {
            Main.getLogger().error("Failed to queue track: " + track.getInfo().title);
        }
    }

    public boolean nextTrack() {
        if (repeatMode == RepeatMode.SINGLE) {
            player.startTrack(player.getPlayingTrack().makeClone(), false);
            return true;
        }
        if (repeatMode == RepeatMode.ALL) {
            queue(player.getPlayingTrack().makeClone());
        }
        AudioTrack nextTrack = queue.poll();
        boolean result = player.startTrack(nextTrack, false);
        if (nextTrack == null) {
            return true;
        }
        return result;
    }

    @Override
    public void onTrackEnd(AudioPlayer player, AudioTrack track, AudioTrackEndReason endReason) {
        if (!endReason.mayStartNext) {
            return;
        }
        nextTrack();
    }

    public void shuffle() {
        List<AudioTrack> list = new ArrayList<>(queue);
        Collections.shuffle(list);
        queue.clear();
        queue.addAll(list);
    }

    public BlockingQueue<AudioTrack> getQueue() {
        return queue;
    }

    public void setRepeatMode(RepeatMode mode) {
        this.repeatMode = mode;
    }

    public RepeatMode getRepeatMode() {
        return repeatMode;
    }

    public enum RepeatMode {
        DISABLE("無効"),
        SINGLE("単一トラック"),
        ALL("すべてのトラック");

        private final String name;

        RepeatMode(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }
}