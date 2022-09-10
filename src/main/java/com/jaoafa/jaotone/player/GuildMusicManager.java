package com.jaoafa.jaotone.player;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;

/**
 * Guild 毎の {@link AudioPlayer} と {@link TrackScheduler} のセットを保持します。
 */
public class GuildMusicManager {
    /**
     * Guild の {@link AudioPlayer}
     */
    public final AudioPlayer player;
    /**
     * Guild の {@link TrackScheduler}
     */
    public final TrackScheduler scheduler;

    /**
     * {@link GuildMusicManager} クラスの新しいインスタンスを初期化します。
     *
     * @param manager {@link AudioPlayerManager}
     */
    public GuildMusicManager(AudioPlayerManager manager) {
        player = manager.createPlayer();
        scheduler = new TrackScheduler(player);
        player.addListener(scheduler);
    }

    /**
     * Guild の {@link AudioPlayer} に該当する {@link AudioPlayerSendHandler} を取得します。
     *
     * @return {@link AudioPlayerSendHandler}
     */
    public AudioPlayerSendHandler getSendHandler() {
        return new AudioPlayerSendHandler(player);
    }
}