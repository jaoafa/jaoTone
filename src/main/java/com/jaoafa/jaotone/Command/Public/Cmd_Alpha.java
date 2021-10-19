package com.jaoafa.jaotone.Command.Public;

import com.jaoafa.jaotone.Framework.Action.ActionId;
import com.jaoafa.jaotone.Framework.Command.Builder.BuildCmd;
import com.jaoafa.jaotone.Framework.Command.Builder.BuildSubCmd;
import com.jaoafa.jaotone.Framework.Command.Builder.BuildSubCmdGroup;
import com.jaoafa.jaotone.Framework.Command.Builder.PackedCmd;
import com.jaoafa.jaotone.Framework.Command.CmdEventContainer;
import com.jaoafa.jaotone.Framework.Command.CmdOptionContainer;
import com.jaoafa.jaotone.Framework.Command.CmdSubstrate;
import com.jaoafa.jaotone.Framework.Lib.LibReply;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.interactions.components.Button;

public class Cmd_Alpha implements CmdSubstrate {
    @Override
    public PackedCmd command() {
        return new BuildCmd("wink", "alpha", "オレをアルファにします。")
                .addSubCmdGroups(
                        new BuildSubCmdGroup("ebi", "エビを食べます。")
                                .addSubCmd(
                                        new BuildSubCmd("normal", "普通にアルファします。")
                                                .addOptions(new OptionData(OptionType.STRING, "powa", "powa", false))
                                                .setFunction(this::ebiNormalAlpha)
                                                .build(),
                                        new BuildSubCmd("super", "めっちょアルファします。")
                                                .setFunction(this::ebiSuperAlpha)
                                                .setPermCheck(member -> false)
                                                .build()
                                ).build(),
                        new BuildSubCmdGroup("phileo", "フィレオを食べます。")
                                .addSubCmd(
                                        new BuildSubCmd("normal", "普通にアルファします。")
                                                .setFunction(this::phileoNormalAlpha)
                                                .build(),
                                        new BuildSubCmd("super", "めっちょアルファします。")
                                                .setFunction(this::phileoSuperAlpha)
                                                .build()
                                ).build()
                ).build();
    }

    void ebiNormalAlpha(JDA jda, Guild guild, MessageChannel channel, ChannelType type, Member member, User user,
                        CmdOptionContainer options, CmdEventContainer events) {
        LibReply.replyEmbeds(
                events,
                new EmbedBuilder()
                        .setTitle(options.getOrDefault("powa", "DefaultTitle").getAsString())
                        .build()
        ).addActionRow(
                Button.danger(new ActionId("powa", "literalpowa", null).get(), "LiteralPowa"),
                Button.danger(new ActionId("powa", "powaliteral", null).get(), Emoji.fromMarkdown("✨"))
        ).done().queue();
    }

    void ebiSuperAlpha(JDA jda, Guild guild, MessageChannel channel, ChannelType type, Member member, User user,
                       CmdOptionContainer options, CmdEventContainer events) {
        LibReply.reply(events, "オ、オオwwwwwwwwオレエビスーパーアルファwwwwwwww最近めっちょふぁぼられてんねんオレwwwwwwwwエゴサとかかけるとめっちょ人気やねんwwwwァァァァァァァwwwクソアルファを見下しながら食べるエビフィレオは一段とウメェなァァァァwwwwwwww");
    }

    void phileoNormalAlpha(JDA jda, Guild guild, MessageChannel channel, ChannelType type, Member member, User user,
                           CmdOptionContainer options, CmdEventContainer events) {
        LibReply.reply(events, "オ、オオwwwwwwwwオレフィレオノーマルアルファwwwwwwww最近めっちょふぁぼられてんねんオレwwwwwwwwエゴサとかかけるとめっちょ人気やねんwwwwァァァァァァァwwwクソアルファを見下しながら食べるエビフィレオは一段とウメェなァァァァwwwwwwww");
    }

    void phileoSuperAlpha(JDA jda, Guild guild, MessageChannel channel, ChannelType type, Member member, User user,
                          CmdOptionContainer options, CmdEventContainer events) {
        LibReply.reply(events, "オ、オオwwwwwwwwオレフィレオスーパーアルファwwwwwwww最近めっちょふぁぼられてんねんオレwwwwwwwwエゴサとかかけるとめっちょ人気やねんwwwwァァァァァァァwwwクソアルファを見下しながら食べるエビフィレオは一段とウメェなァァァァwwwwwwww");
    }
}
