package com.jaoafa.jaotone.command;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import com.jaoafa.jaotone.lib.ToneLib;
import com.jaoafa.jaotone.player.PlayerManager;
import net.dv8tion.jda.api.utils.messages.MessageCreateData;

/**
 * コマンド: queue
 * <p>
 * 現在の再生キューを表示します。1 ページあたり 5 件の曲が表示されます。
 *
 * @see com.jaoafa.jaotone.event.Event_QueueButton
 */
public class Cmd_Queue extends Command {
    /**
     * {@link Cmd_Queue} クラスの新しいインスタンスを初期化します。
     */
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
