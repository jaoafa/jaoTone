package com.jaoafa.jaotone.provider;

import com.github.kiulian.downloader.YoutubeDownloader;
import com.github.kiulian.downloader.downloader.YoutubeCallback;
import com.github.kiulian.downloader.downloader.request.RequestVideoFileDownload;
import com.github.kiulian.downloader.downloader.request.RequestVideoInfo;
import com.github.kiulian.downloader.model.videos.VideoInfo;
import com.github.kiulian.downloader.model.videos.formats.AudioFormat;
import com.jaoafa.jaotone.Main;
import com.jaoafa.jaotone.lib.ToneTrackDetails;
import com.jaoafa.jaotone.lib.ToneTrackDownload;
import com.jaoafa.jaotone.lib.ToneTrackService;
import net.dv8tion.jda.api.entities.Message;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.util.Set;
import java.util.regex.Pattern;

public class YouTubeProvider extends BaseProvider {
    @NotNull
    @Override
    public Set<Pattern> getUrlPatterns() {
        return Set.of(
            Pattern.compile("https?://www\\.youtube\\.com/watch\\?v=(?<videoId>[a-zA-Z\\d_-]{11})"),
            Pattern.compile("https?://youtu\\.be/(?<videoId>[a-zA-Z\\d_-]{11})")
        );
    }

    @NotNull
    @Override
    public String getVideoUrl(@NotNull String videoId) {
        return "https://youtu.be/" + videoId;
    }

    @Override
    public void fetchDetails(String videoId, Message message) {
        Main.getLogger().info("YouTubeProvider#fetchDetails: " + videoId);
        ToneTrackDetails details = ToneTrackDetails.of(ToneTrackService.YOUTUBE, videoId);
        if (details.isLoaded()) {
            details.updateAddVideoMessage(message);
            return;
        }

        YoutubeDownloader downloader = new YoutubeDownloader();
        RequestVideoInfo request = new RequestVideoInfo(videoId)
            .callback(new YoutubeCallback<>() {
                @Override
                public void onFinished(VideoInfo videoInfo) {
                    Main.getLogger().info("YouTubeProvider#fetchDetails#onFinished: " + videoId);

                    if (videoInfo.details().isLive()) {
                        message.editMessage(":x: ライブ配信は非対応です。").queue();
                        return;
                    }
                    if (!videoInfo.details().isDownloadable()) {
                        message.editMessage(":x: ダウンロードできない動画です。").queue();
                        return;
                    }

                    try {
                        ToneTrackDetails
                            .of(ToneTrackService.YOUTUBE, videoId)
                            .title(videoInfo.details().title())
                            .description(videoInfo.details().description())
                            .thumbnailUrl("https://i.ytimg.com/vi/%s/mqdefault.jpg".formatted(videoId))
                            .author(videoInfo.details().author())
                            .save()
                            .updateAddVideoMessage(message);
                    } catch (IOException e) {
                        Main.getLogger().error("Failed to save track details", e);
                    }
                }

                @Override
                public void onError(Throwable throwable) {
                    message.editMessage("動画情報の取得に失敗しました: %s".formatted(throwable.getMessage())).queue();
                }
            })
            .async();
        downloader.getVideoInfo(request);
    }

    @Override
    public void download(String videoId) {
        if (ToneTrackDownload.isDownloaded(ToneTrackService.YOUTUBE, videoId)) {
            return;
        }
        YoutubeDownloader downloader = new YoutubeDownloader();
        RequestVideoInfo request = new RequestVideoInfo(videoId)
            .callback(new YoutubeCallback<>() {
                @Override
                public void onFinished(VideoInfo videoInfo) {
                    AudioFormat format = videoInfo.bestAudioFormat();
                    if (format == null) {
                        Main.getLogger().error("Failed to get audio format: " + videoId);
                        ToneTrackDownload.setFailed(ToneTrackService.YOUTUBE, videoId);
                        return;
                    }
                    RequestVideoFileDownload download = new RequestVideoFileDownload(format)
                        .saveTo(ToneTrackDownload.getServiceDirectory(ToneTrackService.YOUTUBE).toFile())
                        .renameTo(videoId)
                        .overwriteIfExists(true)
                        .callback(new YoutubeCallback<>() {
                            @Override
                            public void onFinished(File file) {
                                Main.getLogger().info("YouTubeProvider#download#onFinished: " + videoId);
                                ToneTrackDownload.setDownloaded(ToneTrackService.YOUTUBE, videoId, file.toPath());
                                ToneTrackDownload.removeDownloading(ToneTrackService.YOUTUBE, videoId);
                            }

                            @Override
                            public void onError(Throwable throwable) {
                                Main.getLogger().error("Failed to download: " + videoId, throwable);
                                ToneTrackDownload.setFailed(ToneTrackService.YOUTUBE, videoId);
                            }
                        });
                    downloader.downloadVideoFile(download);
                }

                @Override
                public void onError(Throwable throwable) {
                    ToneTrackDownload.setFailed(ToneTrackService.YOUTUBE, videoId);
                }
            })
            .async();
        ToneTrackDownload.addDownloading(ToneTrackService.YOUTUBE, videoId);
        downloader.getVideoInfo(request);
    }
}
