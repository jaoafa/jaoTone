package com.jaoafa.jaotone.Action;

import com.jaoafa.jaotone.Framework.Action.ActionSubstrate;
import com.jaoafa.jaotone.Framework.Action.Builder.BuildAction;
import com.jaoafa.jaotone.Framework.Action.Builder.BuildLiteral;
import com.jaoafa.jaotone.Framework.Action.Builder.PackedAction;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.interaction.ButtonClickEvent;
import org.json.JSONObject;

public class Act_Powa implements ActionSubstrate {
    @Override
    public PackedAction action() {
        return new BuildAction("powa")
                .addLiterals(
                        new BuildLiteral("literalpowa")
                                .addPermCheck(member -> member.hasPermission(Permission.MESSAGE_MANAGE))
                                .setFunction(this::literalpowa)
                                .build(),
                        new BuildLiteral("powaliteral")
                                .addPermCheck(member -> false)
                                .setFunction(this::powaliteral)
                                .build()
                ).build();
    }



    private void literalpowa(JDA jda, Guild guild, MessageChannel channel, ChannelType channelType, Member member, User user, JSONObject jsonObject, ButtonClickEvent event) {

    }

    private void powaliteral(JDA jda, Guild guild, MessageChannel channel, ChannelType channelType, Member member, User user, JSONObject jsonObject, ButtonClickEvent event) {
    }
}
