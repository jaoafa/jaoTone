package com.jaoafa.jaotone.Lib.jaoTone.Music;

import com.jaoafa.jaotone.Framework.Lib.LibEmbedColor;
import com.jaoafa.jaotone.Framework.Lib.LibReporter;
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
                if (trackRecord.sendNotify())
                    channel.sendMessageEmbeds(new EmbedBuilder()
                            //todo 対応プラットフォーム毎に表示変更(Description)
                            .setDescription("[YouTubeへ](https://www.youtube.com/watch?v=%s)".formatted(track.getInfo().identifier))
                            .setTitle(":inbox_tray: キューに追加されました！")
                            .addField(":musical_note: タイトル", track.getInfo().title, false)
                            .addField(":bust_in_silhouette: 作者", track.getInfo().author, true)
                            .addField(":timer: 長さ", track.getDuration() / 60000 + "分", true)
                            .setThumbnail(trackRecord.thumbnailUrl())
                            .setColor(LibEmbedColor.SUCCESS)
                            .build()
                    ).queue();
            }

            @Override
            public void playlistLoaded(AudioPlaylist playlist) {
                for (AudioTrack track : playlist.getTracks()) {
                    //todo trackLoadedと同じような処理だけど追加通知は１つのEmbedにまとめる
                }
            }

            @Override
            public void noMatches() {
                channel.sendMessageEmbeds(new EmbedBuilder()
                        .setTitle(":woman_bowing: Not Found")
                        .setDescription("一致する項目が見つかりませんでした！")
                        .setColor(LibEmbedColor.FAILURE)
                        .build()
                ).queue();
            }

            @Override
            public void loadFailed(FriendlyException e) {
                channel.sendMessageEmbeds(new EmbedBuilder()
                        .setTitle(":woman_bowing: Error")
                        .setDescription("ロードに失敗しました！")
                        .setColor(LibEmbedColor.FAILURE)
                        .build()
                ).queue();
                LibReporter.report(e);
            }
        });
    }

    private void play(LibGuildMusic musicManager, AudioTrack track) {
        musicManager.scheduler.queue(track);
    }

}
