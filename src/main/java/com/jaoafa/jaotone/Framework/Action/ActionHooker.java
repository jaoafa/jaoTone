package com.jaoafa.jaotone.Framework.Action;

import net.dv8tion.jda.api.events.interaction.ButtonClickEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

public class ActionHooker extends ListenerAdapter {
    @Override
    public void onButtonClick(@NotNull ButtonClickEvent event) {
        JSONObject identifier = new JSONObject(event.getId());
        String name = identifier.getString("name");
        String literal = identifier.optString("literal",null);
        JSONObject data = identifier.optJSONObject("data");

        ActionRouter.ActionRoutingData routingData = ActionRouter.routeList.stream().filter(actionRoutingData ->
            actionRoutingData.actionName().equals(name) && actionRoutingData.literalName().equals(literal)
        ).findFirst().orElseThrow();

        //private対策(Guildとか)
        //routingData.function().execute(event.getJDA(),event.getGuild(),);
    }
}
