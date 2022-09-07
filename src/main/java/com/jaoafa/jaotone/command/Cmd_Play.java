package com.jaoafa.jaotone.command;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import com.jaoafa.jaotone.lib.ToneLib;
import com.jaoafa.jaotone.player.PlayerManager;

public class Cmd_Play extends Command {
    @SuppressWarnings("unused")
    public Cmd_Play() {
        this.name = "play";
        this.help = "音楽を再生します。";
        this.arguments = "<LinkOrQuery>";
        this.aliases = new String[]{"p", "add"};
    }

    @Override
    protected void execute(CommandEvent event) {
        // クエリはURLか検索ワードのいずれかである
        String query = event.getArgs();
        if (query.isEmpty()) {
            ToneLib.replyError(event, "クエリが指定されていません。");
            return;
        }

        if (!ToneLib.isJoinedVoiceChannel(event.getGuild())) {
            if (event.getMember().getVoiceState() == null || !event.getMember().getVoiceState().inAudioChannel()) {
                ToneLib.replyError(event, "ボイスチャンネルに参加していません。");
                return;
            }
            event.getGuild().getAudioManager().openAudioConnection(event.getMember().getVoiceState().getChannel());
        }


        // クエリがURLかどうかを判定する
        //noinspection HttpUrlsUsage
        if (query.startsWith("http://") || query.startsWith("https://")) {
            // URLの場合
            executeWithUrl(event);
            return;
        }

        // 検索ワードの場合
        executeWithQuery(event);
    }

    private void executeWithUrl(CommandEvent event) {
        String url = event.getArgs();
        PlayerManager.getINSTANCE().loadAndPlay(event, url);
    }

    private void executeWithQuery(CommandEvent event) {
        ToneLib.replyError(event, "not implemented");
    }
}