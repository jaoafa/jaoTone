package com.jaoafa.jaotone.command;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import com.jaoafa.jaotone.lib.ToneLib;
import com.jaoafa.jaotone.player.PlayerManager;
import com.jaoafa.jaotone.player.TrackScheduler;

import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * コマンド: repeat
 * <p>
 * トラックのリピート設定を行います。
 */
public class Cmd_Repeat extends Command {
    /**
     * {@link Cmd_Repeat} クラスの新しいインスタンスを初期化します。
     */
    @SuppressWarnings("unused")
    public Cmd_Repeat() {
        this.name = "repeat";
        this.help = "トラックをリピートします。";
        this.arguments = "<DISABLED|SINGLE|ALL>";
    }

    @Override
    protected void execute(CommandEvent event) {
        if (!ToneLib.isJoinedVoiceChannel(event.getGuild())) {
            ToneLib.replyError(event, "ボイスチャンネルに接続していません。");
            return;
        }

        TrackScheduler scheduler = PlayerManager.getINSTANCE().getGuildMusicManager(event.getGuild()).scheduler;

        if (event.getArgs().isEmpty()) {
            ToneLib.reply(event, "現在のリピート設定: `%s`".formatted(scheduler.getRepeatMode().getName()));
            return;
        }

        TrackScheduler.RepeatMode mode = Arrays
            .stream(TrackScheduler.RepeatMode.values())
            .filter(m -> m.name().equalsIgnoreCase(event.getArgs()))
            .findFirst()
            .orElse(null);

        if (mode == null) {
            ToneLib.replyError(event, "指定されたリピートモードが見つかりませんでした。%s を指定できます。".formatted(
                Arrays
                    .stream(TrackScheduler.RepeatMode.values())
                    .map(TrackScheduler.RepeatMode::name)
                    .map(s -> "`" + s + "`")
                    .collect(
                        Collectors.joining(", "))
            ));
            return;
        }

        scheduler.setRepeatMode(mode);
        event.reactSuccess();
    }
}
