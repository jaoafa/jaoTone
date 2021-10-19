package com.jaoafa.jaotone.Command.jaodev;

import com.jaoafa.jaotone.Framework.Command.Builder.BuildCmd;
import com.jaoafa.jaotone.Framework.Command.Builder.BuildSubCmd;
import com.jaoafa.jaotone.Framework.Command.Builder.BuildSubCmdGroup;
import com.jaoafa.jaotone.Framework.Command.Builder.PackedCmd;
import com.jaoafa.jaotone.Framework.Command.CmdEventContainer;
import com.jaoafa.jaotone.Framework.Command.CmdOptionContainer;
import com.jaoafa.jaotone.Framework.Command.CmdSubstrate;
import com.jaoafa.jaotone.Framework.Lib.LibReply;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

public class Cmd_DevAlpha implements CmdSubstrate {
    @Override
    public PackedCmd command() {
        return new BuildCmd("wink", "devalpha", "オレをアルファにします。")
                .addSubCmdGroups(
                        new BuildSubCmdGroup("ebi", "エビを食べます。")
                                .setPermCheck(member -> true)
                                .addSubCmd(
                                        new BuildSubCmd("normal", "普通にアルファします。")
                                                .addOptions(new OptionData(OptionType.STRING, "powa", "powa", false))
                                                .setFunction(this::ebiNormalAlpha)
                                                .setPermCheck(member -> true)
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
        LibReply.reply(events, "オ、オオwwwwwwwwオレエビノーマルアルファwwwwwwww最近めっちょふぁぼられてんねんオレwwwwwwwwエゴサとかかけるとめっちょ人気やねんwwwwァァァァァァァwwwクソアルファを見下しながら食べるエビフィレオは一段とウメェなァァァァwwwwwwww `" + options.getOrDefault("powa", "nullpowa").getAsString());
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
