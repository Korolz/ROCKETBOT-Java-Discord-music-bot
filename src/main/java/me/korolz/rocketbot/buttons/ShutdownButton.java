package me.korolz.rocketbot.buttons;

import jakarta.annotation.PostConstruct;
import me.korolz.rocketbot.helpers.PresenceChecker;
import me.korolz.rocketbot.interfaces.RocketbotButton;
import me.korolz.rocketbot.lavaplayer.GuildMusicManager;
import me.korolz.rocketbot.lavaplayer.PlayerManager;
import me.korolz.rocketbot.lavaplayer.TrackScheduler;
import me.korolz.rocketbot.listeners.ButtonListener;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ShutdownButton implements RocketbotButton {

    private ButtonListener listener;
    private PlayerManager playerManager;

    @Autowired
    public ShutdownButton(ButtonListener listener, PlayerManager playerManager) {
        this.listener = listener;
        this.playerManager = playerManager;
    }

    @PostConstruct
    public void init() {
        listener.addButton(this);
    }
    @Override
    public String getId() {
        return "SHUTDOWN_BUTTON";
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

        GuildMusicManager guildMusicManager = playerManager.getGuildMusicManager(event.getGuild());
        TrackScheduler trackScheduler = guildMusicManager.getTrackScheduler();
        trackScheduler.getQueue().clear();
        if(trackScheduler.isLoop())
            trackScheduler.toggleLoop();
        trackScheduler.getPlayer().destroy();
        event.reply("**See you next time!**").setEphemeral(true).queue();
        event.getGuild().getAudioManager().closeAudioConnection();
    }
}
