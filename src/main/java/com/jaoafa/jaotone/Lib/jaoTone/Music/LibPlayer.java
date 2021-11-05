package com.jaoafa.jaotone.Lib.jaoTone.Music;

import com.jaoafa.jaotone.Framework.Lib.LibEmbedColor;
import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.TextChannel;

import java.util.HashMap;
import java.util.Map;

public class LibPlayer {
    private static LibPlayer INSTANCE;
    private final AudioPlayerManager playerManager;
    private final Map<Long, LibGuildMusic> musicManagers;


    private LibPlayer() {
        this.musicManagers = new HashMap<>();
        this.playerManager = new DefaultAudioPlayerManager();
        AudioSourceManagers.registerRemoteSources(playerManager);
        AudioSourceManagers.registerLocalSource(playerManager);
    }

    public static synchronized LibPlayer getINSTANCE() {
        if (INSTANCE == null) INSTANCE = new LibPlayer();
        return INSTANCE;
    }

    public synchronized LibGuildMusic getGuildMusicManager(Guild guild) {
        long guildID = guild.getIdLong();
        LibGuildMusic musicManager = musicManagers.get(guildID);
        if (musicManager == null) {
            musicManager = new LibGuildMusic(playerManager);
            musicManagers.put(guildID, musicManager);
        }
        guild.getAudioManager().setSendingHandler(musicManager.getSendHandler());
        return musicManager;
    }

    public void loadAndPlay(TextChannel channel, String trackUrl, LibTrackData trackRecord) {
        LibGuildMusic musicManager = getGuildMusicManager(channel.getGuild());
        playerManager.loadItemOrdered(musicManager, trackUrl, new AudioLoadResultHandler() {
            @Override
            public void trackLoaded(AudioTrack track) {
                track.setUserData(trackRecord);
                play(musicManager, track);
                LibVideoInfo videoInfo = trackRecord.videoInfo();
                if (trackRecord.sendNotify())
                    channel.sendMessageEmbeds(new EmbedBuilder()
                            .setTitle(":inbox_tray: トラックが追加されました！")
                            .setDescription(switch (trackRecord.platformFlag()) {
                                case YouTube, HTTP -> "[ソース:%s を開く](%s)".formatted(trackRecord.platformFlag().name(), trackUrl);
                                case Local -> "ソース:Local `%s`".formatted(trackUrl);
                            })
                            .addField(":pencil: %s".formatted(videoInfo.videoName()), """
                                    **:bust_in_silhouette: 作者:** %s
                                    **:timer: 長さ:** %s 分
                                    """.formatted(videoInfo.authorName(), track.getDuration() / 60000), false)
                            .setThumbnail(videoInfo.imgDefault())
                            .setColor(LibEmbedColor.SUCCESS)
                            .build()
                    ).queue();
            }

            @Override
            public void playlistLoaded(AudioPlaylist playlist) {
                if (trackRecord.sendNotify()) {
                    EmbedBuilder embedBuilder = new EmbedBuilder().setTitle(":inbox_tray: プレイリストが追加されました！");
                    int totalTime = 0;
                    for (AudioTrack track : playlist.getTracks()) {
                        LibVideoInfo videoInfo = trackRecord.videoInfo();
                        embedBuilder.addField(
                                ":pencil: [%s](%s)".formatted(
                                        videoInfo.videoName(),
                                        switch (trackRecord.platformFlag()) {
                                            case YouTube, HTTP -> trackUrl;
                                            case Local -> "";
                                        }
                                ),
                                ":bust_in_silhouette: %s / :timer: %s 分 "
                                        .formatted(
                                                videoInfo.authorName(),
                                                track.getDuration() / 60000
                                        ),
                                false
                        );
                        totalTime = totalTime + Math.toIntExact(track.getDuration() / 60000);
                    }
                    embedBuilder.setDescription("**:timer: 長さ:** %s 分".formatted(totalTime));
                    channel.sendMessageEmbeds(embedBuilder.build()).queue();
                }
            }

            @Override
            public void noMatches() {
                if (trackRecord.sendNotify())
                    channel.sendMessageEmbeds(new EmbedBuilder()
                            .setTitle(":woman_bowing: Not Found")
                            .setDescription("一致する項目が見つかりませんでした！")
                            .setColor(LibEmbedColor.FAILURE)
                            .build()
                    ).queue();
            }

            @Override
            public void loadFailed(FriendlyException e) {
                if (trackRecord.sendNotify())
                    channel.sendMessageEmbeds(new EmbedBuilder()
                            .setTitle(":woman_bowing: Error")
                            .setDescription("ロードに失敗しました！")
                            .setColor(LibEmbedColor.FAILURE)
                            .build()
                    ).queue();
            }
        });
    }

    private void play(LibGuildMusic musicManager, AudioTrack track) {
        musicManager.scheduler.queue(track);
    }
}
