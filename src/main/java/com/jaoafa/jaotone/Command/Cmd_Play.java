package com.jaoafa.jaotone.command;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import com.jaoafa.jaotone.lib.ToneLib;
import com.jaoafa.jaotone.lib.ToneQueue;
import com.jaoafa.jaotone.lib.ToneTrack;
import com.jaoafa.jaotone.lib.ToneTrackService;
import net.dv8tion.jda.api.entities.Message;

import java.util.Arrays;
import java.util.Objects;
import java.util.regex.Matcher;

public class Cmd_Play extends Command {
    public Cmd_Play() {
        this.name = "play";
        this.help = "音楽を再生します。";
        this.arguments = "<LinkOrQuery>";
    }

    @Override
    protected void execute(CommandEvent event) {
        // クエリはURLか検索ワードのいずれかである
        String query = event.getArgs();
        if (query.isEmpty()) {
            ToneLib.replyError(event, "クエリが指定されていません。");
            return;
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

        ToneTrackService provider = Arrays
            .stream(ToneTrackService.values())
            .filter(service -> service
                .getProviderClass()
                .getUrlPatterns()
                .stream()
                .anyMatch(pattern -> pattern.matcher(url).find()))
            .findFirst()
            .orElse(null);
        if (provider == null) {
            ToneLib.replyError(event, "指定されたURLはサポートされていません。");
            return;
        }

        String videoId = provider
            .getProviderClass()
            .getUrlPatterns()
            .stream()
            .map(pattern -> pattern.matcher(url))
            .filter(Matcher::find)
            .map(matcher -> matcher.group("videoId"))
            .filter(Objects::nonNull)
            .findFirst()
            .orElse(null);

        ToneQueue queue = new ToneQueue(event.getGuild());
        queue.add(new ToneTrack(provider, videoId, event.getAuthor()));

        Message message = event.getMessage().reply(":new: 再生キューに追加しました。動画情報を取得しています…。").complete();

        provider.getProviderClass().fetchDetails(videoId, message);
        provider.getProviderClass().download(videoId);
    }

    private void executeWithQuery(CommandEvent event) {
        ToneLib.replyError(event, "not implemented");
    }
}