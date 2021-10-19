package com.jaoafa.jaotone.Framework.Action;

import com.jaoafa.jaotone.Framework.Lib.LibHookerChecks;
import com.jaoafa.jaotone.Framework.Lib.SupportedType;
import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.events.interaction.ButtonClickEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

public class ActionHooker extends ListenerAdapter {
    @Override
    public void onButtonClick(@NotNull ButtonClickEvent event) {
        if (event.getButton() == null) return;
        if (event.getButton().getId() == null) return;
        JSONObject identifier = new JSONObject(event.getButton().getId());

        ActionRouter.ActionRoutingData routingData = ActionRouter.routeList.stream().filter(actionRoutingData ->
                actionRoutingData.actionName().equals(identifier.getString("name")) &&
                        actionRoutingData.literalName().equals(identifier.optString("literal", null))
        ).findFirst().orElseThrow();

        ChannelType channelType = event.getChannelType();

        LibHookerChecks.ChecksResult checksResult = LibHookerChecks.check(
                channelType,
                routingData.supportedType(),
                channelType.equals(ChannelType.TEXT) ? event.getMember() : null,
                routingData.checkPermission()
        );

        switch (checksResult.resultType()) {
            case NotAllowed, ChannelTypeNotSupported -> {
                event.replyEmbeds(checksResult.embed()).queue();
                return;
            }
        }

        routingData.function().execute(
                event.getJDA(),
                channelType.equals(ChannelType.TEXT) ? event.getGuild() : null,
                event.getMessageChannel(),
                channelType,
                channelType.equals(ChannelType.TEXT) ? event.getMember() : null,
                event.getUser(),
                identifier.optJSONObject("data"),
                event
        );
    }
}
