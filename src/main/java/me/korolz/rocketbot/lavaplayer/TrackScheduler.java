package me.korolz.rocketbot.lavaplayer;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventAdapter;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackEndReason;
import lombok.Getter;
import me.korolz.rocketbot.structures.NowPlayingMessage;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

@Getter
public class TrackScheduler extends AudioEventAdapter {

    private final AudioPlayer player;
    private final BlockingQueue<AudioTrack> queue = new LinkedBlockingQueue<>();
    private boolean isLoop = false;
    private final TextChannel musicTextChannel;
    private long messageId = -1;

    public TrackScheduler(AudioPlayer player, Guild guild) {
        this.player = player;
        this.musicTextChannel = guild.getTextChannelsByName("music",true).getFirst();
    }

    public void toggleLoop() {
        isLoop = !isLoop;
    }

    @Override
    public void onTrackStart(AudioPlayer player, AudioTrack track) {
        player.setPaused(false);
        NowPlayingMessage message = updateNowPlayingMessage(track);
        musicTextChannel.sendMessageEmbeds(message.getEmbed())
                .addActionRow(message.getActionRowComponents(isLoop))
                .queue(msg -> messageId = msg.getIdLong());
    }

    @Override
    public void onTrackEnd(AudioPlayer player, AudioTrack track, AudioTrackEndReason endReason) {
        musicTextChannel.deleteMessageById(messageId).queue();
        if(isLoop){
            player.startTrack(track.makeClone(), false);
        } else {
            player.startTrack(queue.poll(), false);
        }
    }

    @Override
    public void onPlayerPause(AudioPlayer player) {
        NowPlayingMessage message = updateNowPlayingMessage(player.getPlayingTrack());
        musicTextChannel.editMessageComponentsById(
                messageId,
                ActionRow.of(message.getActionRowComponents(isLoop))
        ).queue();
    }

    @Override
    public void onPlayerResume(AudioPlayer player) {
        NowPlayingMessage message = updateNowPlayingMessage(player.getPlayingTrack());
        musicTextChannel.editMessageComponentsById(
                messageId,
                ActionRow.of(message.getActionRowComponents(isLoop))
        ).queue();
    }

    public void queue(AudioTrack track) {
        if(!player.startTrack(track, true)) {
            queue.offer(track);
        }
    }

    public NowPlayingMessage updateNowPlayingMessage(AudioTrack track) {
        NowPlayingMessage message = new NowPlayingMessage(player, track);
        message.createEmbed();
        if(!queue.isEmpty())
            message.appendEmbedWithNext(queue.peek(), queue.size());

        return message;
    }

    public void removeQueue() {
        queue.clear();
    }

}

