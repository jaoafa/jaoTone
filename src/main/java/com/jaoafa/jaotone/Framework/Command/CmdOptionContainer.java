package com.jaoafa.jaotone.Framework.Command;

import java.util.ArrayList;

public record CmdOptionContainer(ArrayList<CmdOptionIndex> options) {
    public CmdOptionIndex get(String name) {
        return options.stream().filter(
                cmdOptionIndex -> cmdOptionIndex.optionData().getName().equals(name)
        ).findFirst().orElse(new CmdOptionIndex(null, null));
    }

    public CmdOptionIndex getOrDefault(String name, Object defaultIndex) {
        return options.stream().filter(
                cmdOptionIndex -> cmdOptionIndex.optionData().getName().equals(name)
        ).findFirst().orElse(new CmdOptionIndex(null, defaultIndex));
    }
}
