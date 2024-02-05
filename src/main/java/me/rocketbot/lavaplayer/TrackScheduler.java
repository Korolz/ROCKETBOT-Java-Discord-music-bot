package me.rocketbot.lavaplayer;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventAdapter;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackEndReason;
import me.rocketbot.structures.NowPlayingMessage;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.buttons.Button;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class TrackScheduler extends AudioEventAdapter {

    private static final String musicChannelLabel = "music"; //попробовать со смайликом
    private final AudioPlayer player;
    private final BlockingQueue<AudioTrack> queue = new LinkedBlockingQueue<>();
    private boolean isLoop = false;
    public boolean isLoop() {
        return isLoop;
    }
    public void toggleLoop() {
        isLoop = !isLoop;
    }
    private final TextChannel musicTextChannel;
    private long messageId = -1;

    public TrackScheduler(AudioPlayer player, Guild guild) {
        this.player = player;
        this.musicTextChannel = guild.getTextChannelsByName(musicChannelLabel,true).get(0);
    }

    @Override
    public void onTrackStart(AudioPlayer player, AudioTrack track) {
        player.setPaused(false);
        NowPlayingMessage message = new NowPlayingMessage(track);
        message.createEmbed();
        if(!queue.isEmpty())
            message.appendEmbedWithNext(queue.peek(), queue.size());
        musicTextChannel.sendMessageEmbeds(message.getEmbed()).addActionRow(
                Button.primary("PAUSE_BUTTON", Emoji.fromUnicode("U+23F8")),
                Button.primary("SKIP_BUTTON", Emoji.fromUnicode("U+23ED")),
                Button.secondary("LOOP_BUTTON", Emoji.fromUnicode("U+1F502")),
                Button.link(track.getInfo().uri, "Link")
        ).queue(msg -> messageId = msg.getIdLong());
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
        musicTextChannel.editMessageComponentsById(messageId, ActionRow.of(
                Button.primary("RESUME_BUTTON",Emoji.fromUnicode("U+25B6")),
                Button.primary("SKIP_BUTTON", Emoji.fromUnicode("U+23ED")),
                Button.secondary("LOOP_BUTTON", Emoji.fromUnicode("U+1F502")),
                Button.link(player.getPlayingTrack().getInfo().uri,"Link"))
        ).queue();
    }

    @Override
    public void onPlayerResume(AudioPlayer player) {
        musicTextChannel.editMessageComponentsById(messageId, ActionRow.of(
                Button.primary("PAUSE_BUTTON",Emoji.fromUnicode("U+23F8")),
                Button.primary("SKIP_BUTTON", Emoji.fromUnicode("U+23ED")),
                Button.secondary("LOOP_BUTTON", Emoji.fromUnicode("U+1F502")),
                Button.link(player.getPlayingTrack().getInfo().uri,"Link"))
        ).queue();
    }

    public void queue(AudioTrack track) {
        if(!player.startTrack(track, true)) {
            queue.offer(track);
        }
    }

    public void removeQueue() {
        queue.clear();
    }

    public AudioPlayer getPlayer() {
        return player;
    }

    public BlockingQueue<AudioTrack> getQueue() {
        return queue;
    }
}
