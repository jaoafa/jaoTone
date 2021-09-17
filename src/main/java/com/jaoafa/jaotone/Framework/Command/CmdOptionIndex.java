package com.jaoafa.jaotone.Framework.Command;

import com.jaoafa.jaotone.Lib.jaoTone.LibValue;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import java.util.Optional;
import java.util.regex.Pattern;


public record CmdOptionIndex(OptionData optionData, Object index) {
    //todo OptionType.CHANNELにカテゴリが含まれていたので対応
    //fixme optional、getOrDefaultがあるし要らないことに気が付いたかもしれない

    private static final String SNOWFLAKE_REGEX = "[0-9]+";
    private static final String MENTIONABLE_REGEX = "<@(&??|!??)[0-9]+>|@everyone";

    public String getAsString() {
        return (String) index;
    }

    public String getAsString(String optional) {
        return Optional.ofNullable((String) index).orElse(optional);
    }

    public Boolean getAsBoolean() {
        return Boolean.parseBoolean((String) index);
    }

    public Boolean getAsBoolean(Boolean optional) {
        return Optional.of(Boolean.parseBoolean((String) index)).orElse(optional);
    }

    public GuildChannel getAsGuildChannel() {
        return LibValue.jda.getGuildChannelById(
                Pattern.compile(SNOWFLAKE_REGEX).matcher((String) index).group()
        );
    }

    public GuildChannel getAsGuildChannel(GuildChannel optional) {
        return Optional.ofNullable(LibValue.jda.getGuildChannelById(
                Pattern.compile(SNOWFLAKE_REGEX).matcher((String) index).group()
        )).orElse(optional);
    }

    public Long getAsLong() {
        return Long.parseLong((String) index);
    }

    public Long getAsLong(Long optional) {
        return Optional.of(Long.parseLong((String) index)).orElse(optional);
    }

    public Member getAsMemberWithGuild(String guildId) {
        return LibValue.jda.getGuildById(guildId).getMemberById(
                Pattern.compile(SNOWFLAKE_REGEX).matcher((String) index).group()
        );
    }

    public Member getAsMemberWithGuild(String guildId, Member optional) {
        return Optional.ofNullable(LibValue.jda.getGuildById(guildId).getMemberById(
                Pattern.compile(SNOWFLAKE_REGEX).matcher((String) index).group()
        )).orElse(optional);
    }

    public String getAsMentionable() {
        if (((String) index).matches(MENTIONABLE_REGEX)) {
            return (String) index;
        }
        return null;
    }

    public String getAsMentionable(String optional) {
        if (((String) index).matches(MENTIONABLE_REGEX) && optional.matches(MENTIONABLE_REGEX)) {
            return Optional.of((String) index).orElse(optional);
        }
        return null;
    }

    public TextChannel getAsTextChannel() {
        return LibValue.jda.getTextChannelById(Pattern.compile(SNOWFLAKE_REGEX).matcher((String) index).group());
    }

    public TextChannel getAsTextChannel(TextChannel optional) {
        return Optional.ofNullable(LibValue.jda.getTextChannelById(Pattern.compile(SNOWFLAKE_REGEX).matcher((String) index).group())).orElse(optional);
    }

    public Role getAsRole() {
        return LibValue.jda.getRoleById((String) index);
    }

    public Role getAsRole(Role optional) {
        return Optional.ofNullable(LibValue.jda.getRoleById((String) index)).orElse(optional);
    }

    public User getAsUser() {
        return LibValue.jda.getUserById((String) index);
    }

    public User getAsUser(User optional) {
        return Optional.ofNullable(LibValue.jda.getUserById((String) index)).orElse(optional);
    }
}
