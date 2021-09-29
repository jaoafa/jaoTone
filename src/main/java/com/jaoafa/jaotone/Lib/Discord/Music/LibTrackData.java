package com.jaoafa.jaotone.Lib.Discord.Music;

public record LibTrackData(LoopFlag loopFlag, boolean sendNotify, String thumbnailUrl) {
    public enum LoopFlag {
        QueueLoopStart, QueueLoopEnd, SingleLoop, NoLoop
    }

    public enum PlatformFlag {
        YouTube, HTTP, Local
    }
}
