package com.jaoafa.jaotone.Framework.Command;

import com.jaoafa.jaotone.Lib.jaoTone.LibValue;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import java.util.regex.Pattern;


public record CmdOptionIndex(OptionData optionData, Object index) {
    //todo OptionType.CHANNELにカテゴリが含まれていたので対応

    private static final String SNOWFLAKE_REGEX = "[0-9]+";
    private static final String MENTIONABLE_REGEX = "<@(&??|!??)[0-9]+>|@everyone";

    public String getAsString() {
        return (String) index;
    }

    public Boolean getAsBoolean() {
        return Boolean.parseBoolean((String) index);
    }

    public GuildChannel getAsGuildChannel() {
        return LibValue.jda.getGuildChannelById(
                Pattern.compile(SNOWFLAKE_REGEX).matcher((String) index).group()
        );
    }

    public Long getAsLong() {
        return Long.parseLong((String) index);
    }

    public Member getAsMemberWithGuild(String guildId) {
        return LibValue.jda.getGuildById(guildId).getMemberById(
                Pattern.compile(SNOWFLAKE_REGEX).matcher((String) index).group()
        );
    }

    public String getAsMentionable() {
        if (((String) index).matches(MENTIONABLE_REGEX)) {
            return (String) index;
        }
        return null;
    }

    public TextChannel getAsTextChannel() {
        return LibValue.jda.getTextChannelById(Pattern.compile(SNOWFLAKE_REGEX).matcher((String) index).group());
    }

    public Role getAsRole() {
        return LibValue.jda.getRoleById((String) index);
    }

    public User getAsUser() {
        return LibValue.jda.getUserById((String) index);
    }
}
