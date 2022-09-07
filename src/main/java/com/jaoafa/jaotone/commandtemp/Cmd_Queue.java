package com.jaoafa.jaotone.commandtemp;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import com.jaoafa.jaotone.libtemp.ToneLib;
import com.jaoafa.jaotone.player.PlayerManager;
import net.dv8tion.jda.api.utils.messages.MessageCreateData;

public class Cmd_Queue extends Command {
    @SuppressWarnings("unused")
    public Cmd_Queue() {
        this.name = "queue";
        this.help = "再生キューを表示します。";
        this.arguments = "[page]";
        this.aliases = new String[]{"q"};
    }

    @Override
    protected void execute(CommandEvent event) {
        int page = 1;
        if (!event.getArgs().isEmpty()) {
            try {
                page = Integer.parseInt(event.getArgs());
            } catch (NumberFormatException e) {
                ToneLib.replyError(event, "ページ番号が不正です。");
                return;
            }
        }
        MessageCreateData messageCreateData = PlayerManager.getINSTANCE().getQueueEmbed(event.getGuild(), page);
        if (messageCreateData == null) {
            ToneLib.replyError(event, "キューが空です。");
            return;
        }

        event.getMessage().reply(messageCreateData).queue();
    }
}
