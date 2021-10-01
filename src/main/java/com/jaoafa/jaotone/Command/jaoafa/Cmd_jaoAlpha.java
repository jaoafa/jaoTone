package com.jaoafa.jaotone.Command.jaoafa;

import com.jaoafa.jaotone.Framework.Command.Builder.BuildCmd;
import com.jaoafa.jaotone.Framework.Command.Builder.BuildGroup;
import com.jaoafa.jaotone.Framework.Command.Builder.BuildSubCmd;
import com.jaoafa.jaotone.Framework.Command.CmdEventContainer;
import com.jaoafa.jaotone.Framework.Command.CmdOptionContainer;
import com.jaoafa.jaotone.Framework.Command.CmdSubstrate;
import com.jaoafa.jaotone.Lib.Discord.LibReply;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

public class Cmd_jaoAlpha implements CmdSubstrate {
    @Override
    public BuildCmd builder() {
        return new BuildCmd("jaoalpha", "オレをアルファにします。", member -> member.hasPermission(Permission.MESSAGE_MANAGE))
                .buildWithSubCmdGroup(
                        new BuildGroup("ebi", "エビを食べます。")
                                .addSubCmd(
                                        new BuildSubCmd("normal", "普通にアルファします。", this::ebiNormalAlpha,
                                                new OptionData(OptionType.STRING, "powa", "powa", false)
                                        ),
                                        new BuildSubCmd("super", "めっちょアルファします。", this::ebiSuperAlpha)
                                ),
                        new BuildGroup("phileo", "フィレオを食べます。")
                                .addSubCmd(
                                        new BuildSubCmd("normal", "普通にアルファします。", this::phileoNormalAlpha),
                                        new BuildSubCmd("super", "めっちょアルファします。", this::phileoSuperAlpha)
                                )
                );
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
