package me.rocketbot.buttons;

import me.rocketbot.interfaces.RocketBotButton;
import me.rocketbot.lavaplayer.PlayerManager;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;

public class AppendSong implements RocketBotButton {
    @Override
    public String getId() {
        return "APPEND_SONG";
    }

    @Override
    public void execute(ButtonInteractionEvent event) {
        Member member = event.getMember();
        GuildVoiceState memberVoiceState = member.getVoiceState();

        if(!memberVoiceState.inAudioChannel()) { //checks presence of a member
            event.reply("You need to be in a voice channel").setEphemeral(true).queue();
            return;
        }

        Member self = event.getGuild().getSelfMember();
        GuildVoiceState selfVoiceState = self.getVoiceState();

        if(!selfVoiceState.inAudioChannel()) { //checks presence of a member in a same channel
            event.getGuild().getAudioManager().openAudioConnection(memberVoiceState.getChannel());
        } else {
            if(selfVoiceState.getChannel() != memberVoiceState.getChannel()) {
                event.reply("You are not in the same channel as me").setEphemeral(true).queue();
                return;
            }
        }

        PlayerManager playerManager = PlayerManager.get();
        String request = event.getMessage().getButtons().get(1).getId(); //fetching link through the link button
        playerManager.play(event.getGuild(), request);
        event.reply("Song queued again!").setEphemeral(true).queue();
    }
}
