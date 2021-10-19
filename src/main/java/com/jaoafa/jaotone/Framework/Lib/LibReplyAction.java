package com.jaoafa.jaotone.Framework.Lib;

import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.Component;
import net.dv8tion.jda.api.requests.RestAction;
import net.dv8tion.jda.api.requests.restaction.MessageAction;
import net.dv8tion.jda.api.requests.restaction.interactions.ReplyAction;
import net.dv8tion.jda.api.utils.AttachmentOption;

import javax.annotation.Nonnull;
import java.io.File;
import java.io.InputStream;

public class LibReplyAction {
    private final Object action;

    public LibReplyAction(Object action) {
        if (!(action instanceof ReplyAction || action instanceof MessageAction))
            throw new UnsupportedOperationException("Unsupported Action Type!");
        this.action = action;
    }

    public LibReplyAction addActionRow(Component... components) {
        if (action instanceof ReplyAction)
            ((ReplyAction) action).addActionRow(components);
        if (action instanceof MessageAction)
            ((MessageAction) action).setActionRow(components);

        return this;
    }

    public LibReplyAction addActionRows(ActionRow... actionRows) {
        if (action instanceof ReplyAction)
            ((ReplyAction) action).addActionRows(actionRows);
        if (action instanceof MessageAction)
            ((MessageAction) action).setActionRows(actionRows);

        return this;
    }

    public LibReplyAction addEmbeds(MessageEmbed... embeds) {
        if (action instanceof ReplyAction)
            ((ReplyAction) action).addEmbeds(embeds);
        if (action instanceof MessageAction)
            ((MessageAction) action).setEmbeds(embeds);

        return this;
    }

    public LibReplyAction addFile(@Nonnull File file, @Nonnull AttachmentOption... options) {
        if (action instanceof ReplyAction)
            ((ReplyAction) action).addFile(file, options);
        if (action instanceof MessageAction)
            ((MessageAction) action).addFile(file, options);

        return this;
    }

    public LibReplyAction addFile(@Nonnull File file, @Nonnull String name, @Nonnull AttachmentOption... options) {
        if (action instanceof ReplyAction)
            ((ReplyAction) action).addFile(file, name, options);
        if (action instanceof MessageAction)
            ((MessageAction) action).addFile(file, name, options);

        return this;
    }

    public LibReplyAction addFile(@Nonnull byte[] data, @Nonnull String name, @Nonnull AttachmentOption... options) {
        if (action instanceof ReplyAction)
            ((ReplyAction) action).addFile(data, name, options);
        if (action instanceof MessageAction)
            ((MessageAction) action).addFile(data, name, options);

        return this;
    }

    public LibReplyAction addFile(@Nonnull InputStream var1, @Nonnull String var2, @Nonnull AttachmentOption... var3) {
        if (action instanceof ReplyAction)
            ((ReplyAction) action).addFile(var1, var2, var3);
        if (action instanceof MessageAction)
            ((MessageAction) action).addFile(var1, var2, var3);

        return this;
    }

    public RestAction done() {
        if (action instanceof ReplyAction)
            return ((ReplyAction) action);
        if (action instanceof MessageAction)
            return ((MessageAction) action);
        throw new UnsupportedOperationException("Action type invalid!");
    }
}
