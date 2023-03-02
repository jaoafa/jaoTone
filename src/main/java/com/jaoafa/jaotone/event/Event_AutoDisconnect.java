package com.jaoafa.jaotone.event;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.channel.unions.AudioChannelUnion;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceUpdateEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

/**
 * 自動切断
 * <p>
 * 以下の時に切断
 * <ul>
 *   <li>切断元のボイスチャンネルにBot自身が参加している</li>
 *   <li>切断元のボイスチャンネルにBot以外の一般ユーザーが参加していない</li>
 * </ul>
 */
public class Event_AutoDisconnect extends ListenerAdapter {
    @Override
    public void onGuildVoiceUpdate(GuildVoiceUpdateEvent event) {
        if (event.getChannelLeft() == null) {
            return;
        }
        handler(event.getGuild(), event.getChannelLeft());
    }

    void handler(Guild guild, AudioChannelUnion channel) {
        if (!channel.getMembers().contains(guild.getSelfMember())) {
            return;
        }
        if (channel.getMembers().stream().anyMatch(member -> !member.getUser().isBot())) {
            return;
        }
        guild.getAudioManager().closeAudioConnection();
    }
}
