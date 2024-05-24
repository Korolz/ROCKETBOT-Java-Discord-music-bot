package me.korolz.rocketbot.buttons;

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackInfo;
import jakarta.annotation.PostConstruct;
import me.korolz.rocketbot.helpers.PresenceChecker;
import me.korolz.rocketbot.interfaces.RocketbotButton;
import me.korolz.rocketbot.lavaplayer.GuildMusicManager;
import me.korolz.rocketbot.lavaplayer.PlayerManager;
import me.korolz.rocketbot.listeners.ButtonListener;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class QueueButton implements RocketbotButton {

    private ButtonListener listener;
    private PlayerManager playerManager;

    @Autowired
    public QueueButton(ButtonListener listener, PlayerManager playerManager) {
        this.listener = listener;
        this.playerManager = playerManager;
    }

    @PostConstruct
    public void init() {
        listener.addButton(this);
    }
    @Override
    public String getId() {
        return "QUEUE_BUTTON";
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
        List<AudioTrack> queue = new ArrayList<>(guildMusicManager.getTrackScheduler().getQueue());
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setColor(16121855);
        embedBuilder.setTitle("**Current Queue**");
        if(queue.isEmpty()) {
            embedBuilder.setDescription("Queue is empty");
        }
        for(int i = 0; i < queue.size() && i < 10; i++) {
            AudioTrackInfo info = queue.get(i).getInfo();
            embedBuilder.addField(i+1 + ":", info.title, false);
        }
        embedBuilder.addField("**Queue Size**:", String.valueOf(queue.size()),false);
        event.replyEmbeds(embedBuilder.build()).setEphemeral(true).queue();
    }
}
