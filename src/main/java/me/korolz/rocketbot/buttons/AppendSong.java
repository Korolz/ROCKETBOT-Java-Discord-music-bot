package me.korolz.rocketbot.buttons;

import jakarta.annotation.PostConstruct;
import me.korolz.rocketbot.helpers.PresenceChecker;
import me.korolz.rocketbot.interfaces.RocketbotButton;
import me.korolz.rocketbot.lavaplayer.PlayerManager;
import me.korolz.rocketbot.listeners.ButtonListener;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AppendSong implements RocketbotButton {

    private ButtonListener listener;
    private PlayerManager playerManager;

    @Autowired
    public AppendSong(ButtonListener listener, PlayerManager playerManager) {
        this.listener = listener;
        this.playerManager = playerManager;
    }

    @PostConstruct
    public void init() {
        listener.addButton(this);
    }
    @Override
    public String getId() {
        return "APPEND_SONG";
    }

    @Override
    public void execute(ButtonInteractionEvent event) {
        if(!PresenceChecker.isMemberInChannel(event)){
            event.reply("You need to be in a voice channel").setEphemeral(true).queue();
            return;
        }
        if(PresenceChecker.isBotInChannel(event)){
            if(!PresenceChecker.isBothInSameChannel(event)){
                event.reply("You are not in the same channel as me").setEphemeral(true).queue();
                return;
            }
        }
        else {
            Member member = event.getMember();
            GuildVoiceState memberVoiceState = member.getVoiceState();
            event.getGuild().getAudioManager().openAudioConnection(memberVoiceState.getChannel());
        }

        String request = event.getMessage().getButtons().get(1).getId(); //fetching link through the link button
        playerManager.play(event.getGuild(), request);
        event.reply("Song queued again!").setEphemeral(true).queue();
    }
}
