package com.jaoafa.jaotone.event;

import net.dv8tion.jda.api.entities.AudioChannel;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceLeaveEvent;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceMoveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import javax.annotation.Nonnull;

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
    public void onGuildVoiceLeave(@Nonnull GuildVoiceLeaveEvent event) {
        handler(event.getGuild(), event.getChannelLeft());
    }

    @Override
    public void onGuildVoiceMove(@Nonnull GuildVoiceMoveEvent event) {
        handler(event.getGuild(), event.getChannelLeft());
    }

    void handler(Guild guild, AudioChannel channel) {
        if (!channel.getMembers().contains(guild.getSelfMember())) {
            return;
        }
        if (channel.getMembers().stream().anyMatch(member -> !member.getUser().isBot())) {
            return;
        }
        guild.getAudioManager().closeAudioConnection();
    }
}
