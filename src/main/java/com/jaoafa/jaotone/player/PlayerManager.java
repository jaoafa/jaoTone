package com.jaoafa.jaotone.player;

import com.jagrosh.jdautilities.command.CommandEvent;
import com.jaoafa.jaotone.Main;
import com.jaoafa.jaotone.lib.ToneLib;
import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.dv8tion.jda.api.utils.messages.MessageCreateBuilder;
import net.dv8tion.jda.api.utils.messages.MessageCreateData;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.concurrent.BlockingQueue;

/**
 * トラックプレイヤーを管理するクラスです。
 */
public class PlayerManager {
    private static final AudioPlayerManager playerManager;
    private static final Map<Long, GuildMusicManager> musicManagers = new HashMap<>();

    static {
        playerManager = new DefaultAudioPlayerManager();
        AudioSourceManagers.registerRemoteSources(playerManager);
        AudioSourceManagers.registerLocalSource(playerManager);
    }

    /**
     * 指定された {@link Guild} に対応する {@link GuildMusicManager} を取得します。既存のインスタンスが存在しない場合は新しいインスタンスを生成します。
     *
     * @param guild 対象の {@link Guild}
     * @return {@link GuildMusicManager} クラスのインスタンス
     */
    public static GuildMusicManager getGuildMusicManager(Guild guild) {
        long guildID = guild.getIdLong();
        GuildMusicManager musicManager = musicManagers.get(guildID);
        if (musicManager == null) {
            musicManager = new GuildMusicManager(playerManager);
            musicManagers.put(guildID, musicManager);
        }
        guild.getAudioManager().setSendingHandler(musicManager.getSendHandler());
        return musicManager;
    }

    /**
     * 指定された {@link Guild} の GuildMusicManager を削除します。
     *
     * @param guild 対象の {@link Guild}
     */
    public static void destroyGuildMusicManager(Guild guild) {
        GuildMusicManager manager = PlayerManager.getGuildMusicManager(guild);
        manager.player.stopTrack();
        manager.scheduler.getQueue().clear();
        musicManagers.remove(guild.getIdLong());
    }

    /**
     * 指定したトラックをロード・再生するようにキューに追加します。<br>
     * プレイリストの URL を指定した場合は、プレイリストの全トラックをキューに追加します。<br>
     * <code>ytsearch:</code> または <code>scsearch:</code> を先頭に付けた場合は、検索結果の1件目をキューに追加します。
     *
     * @param event    コマンド実行時の {@link CommandEvent}
     * @param trackUrl トラックの URL または検索クエリ
     * @param adder    トラックを追加したユーザー
     */
    public static void loadAndPlay(CommandEvent event, String trackUrl, User adder) {
        GuildMusicManager musicManager = getGuildMusicManager(event.getGuild());
        playerManager.loadItemOrdered(musicManager, trackUrl, new AudioLoadResultHandler() {
            @Override
            public void trackLoaded(AudioTrack track) {
                Main.getLogger().info("trackLoaded: " + track.getInfo().title + " (" + track.getInfo().uri + ")");
                track.setUserData(adder);
                musicManager.scheduler.queue(track);
                event.reactSuccess();
            }

            @Override
            public void playlistLoaded(AudioPlaylist playlist) {
                if (playlist.isSearchResult()) {
                    AudioTrack selectTrack = playlist.getTracks().get(0);
                    trackLoaded(selectTrack);
                    ToneLib.reply(event, "検索結果の一件目 `%s` を再生します。".formatted(selectTrack.getInfo().title));
                    return;
                }
                Main.getLogger().info("playlistLoaded: " + playlist.getName() + " / " + playlist.getTracks().size());
                for (AudioTrack track : playlist.getTracks()) {
                    track.setUserData(adder);
                    musicManager.scheduler.queue(track);
                }
                ToneLib.reply(event, "プレイリストから%d件を再生キューに追加しました。".formatted(playlist.getTracks().size()));
                event.reactSuccess();
            }

            @Override
            public void noMatches() {
                Main.getLogger().info("noMatches");
                ToneLib.replyError(event, "指定されたクエリにマッチするトラックが見つかりませんでした。");
            }

            @Override
            public void loadFailed(FriendlyException e) {
                Main.getLogger().info("loadFailed: " + e.getMessage());
                ToneLib.replyError(event,
                        "トラックの読み込みに失敗しました: `%s(%s) -> %s`".formatted(e.getClass().getSimpleName(),
                                e.severity.name(),
                                e.getMessage()));
            }
        });
    }

    /**
     * 指定した {@link Guild} の再生キュー Embed を取得します。
     *
     * @param guild 対象の {@link Guild}
     * @param page  ページ番号
     * @return 再生キュー Embed
     */
    @Nullable
    public static MessageCreateData getQueueEmbed(Guild guild, int page) {
        GuildMusicManager manager = PlayerManager.getGuildMusicManager(guild);
        LinkedList<AudioTrack> tracks = new LinkedList<>();
        if (manager.player.getPlayingTrack() != null) {
            tracks.add(manager.player.getPlayingTrack());
        }
        BlockingQueue<AudioTrack> queue = manager.scheduler.getQueue();
        tracks.addAll(queue);
        if (tracks.isEmpty()) {
            return null;
        }

        EmbedBuilder builder = new EmbedBuilder().setTitle("再生キュー");

        int num = ((page - 1) * 5) + 1;
        for (AudioTrack track : tracks.stream().skip((page - 1) * 5L).limit(5).toList()) {
            String fieldTitle = "%d. `%s`".formatted(num, track.getInfo().title);
            String url = track.getInfo().uri;
            User adder = track.getUserData(User.class);
            String fieldText = "Author: `%s`\nURL: %s\nAdder: %s".formatted(track.getInfo().author,
                    url,
                    adder.getAsMention());
            builder.addField(fieldTitle, fieldText, false);
            num++;
        }

        builder.setFooter("Page %d/%d".formatted(page, (int) Math.ceil((double) tracks.size() / 5)));

        Button prev = Button.secondary("queue:" + (page - 1), "前のページ").withDisabled(page == 1);
        Button next = Button
                .secondary("queue:" + (page + 1), "次のページ")
                .withDisabled(page == (int) Math.ceil((double) tracks.size() / 5));

        return new MessageCreateBuilder().setEmbeds(builder.build()).addActionRow(prev, next).build();
    }
}