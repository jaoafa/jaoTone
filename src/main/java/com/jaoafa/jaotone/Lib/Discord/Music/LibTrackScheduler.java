package com.jaoafa.jaotone.Lib.Discord.Music;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventAdapter;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackEndReason;
import net.dv8tion.jda.api.entities.Guild;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class LibTrackScheduler extends AudioEventAdapter {
    private static final Map<String, Map.Entry<AudioPlayer, LibPlayer>> players = new HashMap<>();
    private final AudioPlayer player;
    private final BlockingQueue<AudioTrack> queue;

    public LibTrackScheduler(AudioPlayer player) {
        this.player = player;
        this.queue = new LinkedBlockingQueue<>();
    }

    public void queue(AudioTrack track) {
        if (!player.startTrack(track, true)) queue.offer(track);
    }

    public void nextTrack() {
        player.startTrack(queue.poll(), false);
    }

    private LibPlayer getTrackManager(Guild guild) {
        return players.get(guild.getId()).getValue();
    }

    @Override
    public void onTrackEnd(AudioPlayer player, AudioTrack track, AudioTrackEndReason endReason) {

    }

}
