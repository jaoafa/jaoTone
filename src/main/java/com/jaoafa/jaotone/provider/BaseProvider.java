package com.jaoafa.jaotone.provider;

import net.dv8tion.jda.api.entities.Message;

import javax.annotation.Nonnull;
import java.util.Set;
import java.util.regex.Pattern;

public abstract class BaseProvider {
    /**
     * このプロバイダーが処理するURLのパターンを返します。
     * videoId として動画の ID が取得できるようにしてください。
     *
     * @return URLのパターン
     */
    @Nonnull
    public abstract Set<Pattern> getUrlPatterns();

    /**
     * このプロバイダーにおける動画 URL を返します。
     *
     * @param videoId 動画の ID
     * @return 動画 URL
     */
    @Nonnull
    public abstract String getVideoUrl(@Nonnull String videoId);

    /**
     * 動画詳細を取得し、トラックプロパティとして適用します。
     * このメソッドは同期的に呼び出されますが、処理は非同期で行う必要があります。
     */
    public abstract void fetchDetails(String videoId, Message message);

    public abstract void download(String videoId);
}
