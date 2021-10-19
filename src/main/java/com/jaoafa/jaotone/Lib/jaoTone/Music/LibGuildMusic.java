package com.jaoafa.jaotone.Lib.jaoTone.Music;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;

public class LibGuildMusic {
    public final AudioPlayer player;

    public final LibTrackScheduler scheduler;

    public LibGuildMusic(AudioPlayerManager manager) {
        player = manager.createPlayer();
        scheduler = new LibTrackScheduler(player);
        player.addListener(scheduler);
    }

    public LibAudioSender getSendHandler() {
        return new LibAudioSender(player);
    }
}
