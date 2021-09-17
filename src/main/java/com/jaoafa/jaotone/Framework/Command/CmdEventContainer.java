package com.jaoafa.jaotone.Framework.Command;

import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public record CmdEventContainer(SlashCommandEvent appEvent, MessageReceivedEvent textEvent, CmdType type) {
}
