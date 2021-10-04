package com.jaoafa.jaotone.Framework.Action;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.interaction.ButtonClickEvent;
import org.json.JSONObject;

@FunctionalInterface
public interface ActionFunction {
    void execute(JDA jda,
                 Guild guild,
                 MessageChannel channel,
                 ChannelType type,
                 Member member,
                 User user,
                 JSONObject data,
                 ButtonClickEvent event);
}
