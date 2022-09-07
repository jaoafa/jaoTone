package com.jaoafa.jaotone.commandtemp;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import com.jaoafa.jaotone.libtemp.ToneLib;
import com.jaoafa.jaotone.player.PlayerManager;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import net.dv8tion.jda.api.EmbedBuilder;

import java.awt.*;

public class Cmd_NowPlaying extends Command {
    @SuppressWarnings("unused")
    public Cmd_NowPlaying() {
        this.name = "nowplaying";
        this.help = "再生中のトラックを表示します。";
        this.arguments = "";
        this.aliases = new String[]{"np", "now"};
    }

    @Override
    protected void execute(CommandEvent event) {
        if (!ToneLib.isJoinedVoiceChannel(event.getGuild())) {
            ToneLib.replyError(event, "ボイスチャンネルに接続していません。");
            return;
        }

        AudioTrack track = PlayerManager
            .getINSTANCE()
            .getGuildMusicManager(event.getGuild()).player.getPlayingTrack();
        if (track == null) {
            ToneLib.replyError(event, "再生中のトラックがありません。");
            return;
        }
        EmbedBuilder embed = new EmbedBuilder()
            .setTitle(track.getInfo().title)
            .setAuthor(track.getInfo().author)
            .setColor(Color.GREEN)
            .addField("Time",
                      ToneLib.formatTime(track.getPosition()) + " / " + ToneLib.formatTime(track.getDuration()),
                      false);

        if (track.getInfo().uri.startsWith("https://www.youtube.com/watch?v=")) {
            embed.setThumbnail("https://i.ytimg.com/vi/" + track.getInfo().identifier + "/mqdefault.jpg");
        }

        event.getMessage().replyEmbeds(embed.build()).queue();
    }
}