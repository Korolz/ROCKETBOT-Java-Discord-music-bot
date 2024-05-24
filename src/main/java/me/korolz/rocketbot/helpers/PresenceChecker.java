package me.korolz.rocketbot.helpers;

import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.interaction.GenericInteractionCreateEvent;

public class PresenceChecker {
    public static boolean isMemberInChannel(GenericInteractionCreateEvent event) {
        Member member = event.getMember();
        GuildVoiceState memberVoiceState = member.getVoiceState();

        if (!memberVoiceState.inAudioChannel()) { //checks presence of a member
            return false;
        } else
            return true;
    }

    public static boolean isBotInChannel(GenericInteractionCreateEvent event) {
        Member self = event.getGuild().getSelfMember();
        GuildVoiceState selfVoiceState = self.getVoiceState();

        return selfVoiceState.inAudioChannel();
    }

    public static boolean isBothInSameChannel(GenericInteractionCreateEvent event) {
        Member self = event.getGuild().getSelfMember();
        GuildVoiceState selfVoiceState = self.getVoiceState();

        Member member = event.getMember();
        GuildVoiceState memberVoiceState = member.getVoiceState();

        if (selfVoiceState.getChannel() != memberVoiceState.getChannel()) {
            return false;
        }
        else
            return true;
    }
}
