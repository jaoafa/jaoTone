package com.jaoafa.jaotone.Lib.jaoTone.Music;

public record LibTrackData(LoopFlag loopFlag, boolean sendNotify, String thumbnailUrl) {
    public enum LoopFlag {
        QueueLoopStart, QueueLoopEnd, SingleLoop, NoLoop
    }

    public enum PlatformFlag {
        YouTube, HTTP, Local
    }
}
