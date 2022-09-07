package com.jaoafa.jaotone.lib;

import net.dv8tion.jda.api.entities.User;

public record ToneTrack(ToneTrackService service, String videoId, User adder) {
}
