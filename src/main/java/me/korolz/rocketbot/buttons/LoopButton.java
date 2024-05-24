package me.korolz.rocketbot.buttons;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import jakarta.annotation.PostConstruct;
import me.korolz.rocketbot.helpers.PresenceChecker;
import me.korolz.rocketbot.interfaces.RocketbotButton;
import me.korolz.rocketbot.lavaplayer.GuildMusicManager;
import me.korolz.rocketbot.lavaplayer.PlayerManager;
import me.korolz.rocketbot.listeners.ButtonListener;
import me.korolz.rocketbot.structures.NowPlayingMessage;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class LoopButton implements RocketbotButton {

    private ButtonListener listener;
    private PlayerManager playerManager;

    @Autowired
    public LoopButton(ButtonListener listener, PlayerManager playerManager) {
        this.listener = listener;
        this.playerManager = playerManager;
    }

    @PostConstruct
    public void init() {
        listener.addButton(this);
    }
    @Override
    public String getId() {
        return "LOOP_BUTTON";
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
        guildMusicManager.getTrackScheduler().toggleLoop();
        AudioPlayer player = guildMusicManager.getTrackScheduler().getPlayer();

        NowPlayingMessage message = new NowPlayingMessage(player, player.getPlayingTrack());
        event.getChannel().editMessageComponentsById(
                guildMusicManager.getTrackScheduler().getMessageId(),
                ActionRow.of(message.getActionRowComponents(guildMusicManager.getTrackScheduler().isLoop()))
        ).queue();
        event.reply("**Loop** status: " + guildMusicManager.getTrackScheduler().isLoop()).setEphemeral(true).queue();
    }
}
