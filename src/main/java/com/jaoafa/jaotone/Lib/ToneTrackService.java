package com.jaoafa.jaotone.lib;

import com.jaoafa.jaotone.provider.BaseProvider;
import com.jaoafa.jaotone.provider.YouTubeProvider;

public enum ToneTrackService {
    YOUTUBE("YouTube", new YouTubeProvider());

    private final String name;
    private final BaseProvider providerClass;

    ToneTrackService(String name, BaseProvider providerClass) {
        this.name = name;
        this.providerClass = providerClass;
    }

    public String getName() {
        return name;
    }

    public BaseProvider getProviderClass() {
        return providerClass;
    }
}
