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

/**
 * トラックの再生を管理（スケジューリング）します。
 */
public class TrackScheduler extends AudioEventAdapter {
    private final AudioPlayer player;
    private final BlockingQueue<AudioTrack> queue;
    private RepeatMode repeatMode = RepeatMode.DISABLE;

    /**
     * {@link TrackScheduler} クラスの新しいインスタンスを初期化します。
     *
     * @param player {@link AudioPlayer}
     */
    public TrackScheduler(AudioPlayer player) {
        this.player = player;
        this.queue = new LinkedBlockingQueue<>();
    }

    /**
     * トラックをキューに追加します。
     *
     * @param track {@link AudioTrack}
     */
    public void queue(AudioTrack track) {
        if (player.startTrack(track, true)) {
            return;
        }
        if (!queue.offer(track)) {
            Main.getLogger().error("Failed to queue track: " + track.getInfo().title);
        }
    }

    /**
     * 次のトラックを再生します。
     *
     * @return 再生できたかどうか
     */
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

    /**
     * トラックを終了したときに呼び出されます。
     *
     * @param player    {@link AudioPlayer}
     * @param track     {@link AudioTrack}
     * @param endReason {@link AudioTrackEndReason}
     */
    @Override
    public void onTrackEnd(AudioPlayer player, AudioTrack track, AudioTrackEndReason endReason) {
        if (!endReason.mayStartNext) {
            return;
        }
        nextTrack();
    }

    /**
     * キューをシャッフルします。
     *
     * @see com.jaoafa.jaotone.command.Cmd_Shuffle
     */
    public void shuffle() {
        List<AudioTrack> list = new ArrayList<>(queue);
        Collections.shuffle(list);
        queue.clear();
        queue.addAll(list);
    }

    /**
     * キューを取得します。
     *
     * @return キュー
     * @see com.jaoafa.jaotone.command.Cmd_Queue
     */
    public BlockingQueue<AudioTrack> getQueue() {
        return queue;
    }

    /**
     * リピートモードを設定します。
     *
     * @param mode リピートモード
     * @see com.jaoafa.jaotone.command.Cmd_Repeat
     */
    public void setRepeatMode(RepeatMode mode) {
        this.repeatMode = mode;
    }

    /**
     * リピートモードを取得します。
     *
     * @return 現在のリピートモード
     * @see com.jaoafa.jaotone.command.Cmd_Repeat
     */
    public RepeatMode getRepeatMode() {
        return repeatMode;
    }

    /**
     * リピートモード
     *
     * @see com.jaoafa.jaotone.command.Cmd_Repeat
     */
    public enum RepeatMode {
        /**
         * リピート無効
         */
        DISABLE("無効"),
        /**
         * 単曲リピート
         */
        SINGLE("単一トラック"),
        /**
         * 全曲リピート
         */
        ALL("すべてのトラック");

        private final String name;

        RepeatMode(String name) {
            this.name = name;
        }

        /**
         * リピートモードの名前を取得します。
         *
         * @return リピートモードの名前
         */
        public String getName() {
            return name;
        }
    }
}