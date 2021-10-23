package com.jaoafa.jaotone.Lib.jaoTone.Music;

public record LibTrackData(LoopFlag loopFlag, PlatformFlag platformFlag,
                           boolean sendNotify, LibVideoInfo videoInfo) {
    public enum LoopFlag {
        QueueLoopStart, QueueLoopEnd, SingleLoop, NoLoop
    }

    public enum PlatformFlag {
        YouTube, HTTP, Local
    }
}
