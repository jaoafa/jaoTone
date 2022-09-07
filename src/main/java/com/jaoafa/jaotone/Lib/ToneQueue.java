package com.jaoafa.jaotone.lib;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.dv8tion.jda.api.utils.messages.MessageCreateBuilder;
import net.dv8tion.jda.api.utils.messages.MessageCreateData;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

/**
 * トラックキュー
 * <p>
 * 動画の詳細情報などはこのキューでは管理しない。{@link ToneTrackDetails} を使用する。
 */
public class ToneQueue {
    private static final Map<Long, LinkedList<ToneTrack>> queues = new HashMap<>();

    private final Guild guild;

    public ToneQueue(Guild guild) {
        this.guild = guild;
    }

    public void add(ToneTrack track) {
        LinkedList<ToneTrack> queue = queues.computeIfAbsent(guild.getIdLong(), k -> new LinkedList<>());
        queue.add(track);
        queues.put(guild.getIdLong(), queue);
    }

    public void addFirst(ToneTrack track) {
        LinkedList<ToneTrack> queue = queues.computeIfAbsent(guild.getIdLong(), k -> new LinkedList<>());
        queue.addFirst(track);
        queues.put(guild.getIdLong(), queue);
    }

    public void remove(ToneTrack track) {
        LinkedList<ToneTrack> queue = queues.computeIfAbsent(guild.getIdLong(), k -> new LinkedList<>());
        queue.remove(track);
        queues.put(guild.getIdLong(), queue);
    }

    public void clear() {
        queues.remove(guild.getIdLong());
    }

    @Nullable
    public Queue<ToneTrack> getQueue() {
        return queues.get(guild.getIdLong());
    }

    public boolean isEmpty() {
        return queues.get(guild.getIdLong()) == null || queues.get(guild.getIdLong()).isEmpty();
    }

    public ToneTrack peek() {
        if (isEmpty()) {
            return null;
        }
        return queues.get(guild.getIdLong()).peek();
    }

    @Nullable
    public static MessageCreateData getQueueEmbed(Guild guild, int page) {
        ToneQueue queue = new ToneQueue(guild);
        Queue<ToneTrack> tracks = queue.getQueue();
        if (tracks == null || tracks.isEmpty()) {
            return null;
        }

        EmbedBuilder builder = new EmbedBuilder().setTitle("再生キュー");

        int num = ((page - 1) * 5) + 1;
        for (ToneTrack track : tracks.stream().skip((page - 1) * 5L).limit(5).toList()) {
            ToneTrackDetails details = ToneTrackDetails.of(track);
            String fieldTitle = "%d. [%s] %s".formatted(num,
                                                        track.service().getName(),
                                                        details.isLoaded() ? details.getTitle() : track.videoId());
            String url = track.service().getProviderClass().getVideoUrl(track.videoId());
            String fieldText = details.isLoaded() ?
                               "Author: %s\nURL: %s".formatted(details.getAuthor(), url) :
                               "読み込み中...";
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
