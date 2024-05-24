package me.korolz.rocketbot.structures;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.interactions.components.buttons.Button;

import java.util.ArrayList;
import java.util.Collection;
//try to inject to application context
public class NowPlayingMessage {
    private final AudioPlayer player;
    private final AudioTrack track;
    private final EmbedBuilder embedBuilder = new EmbedBuilder();

    public NowPlayingMessage(AudioPlayer player, AudioTrack track){
        this.player = player;
        this.track = track;
    }

    public void createEmbed(){
        embedBuilder.setColor(7198717);
        embedBuilder.setTitle("*Now Playing...*");
        embedBuilder.addField(track.getInfo().title, track.getInfo().author, true);
        embedBuilder.setThumbnail(track.getInfo().artworkUrl);
        if(track.getDuration() < 39600000L)
            embedBuilder.setFooter(this.getTimeDuration(track.getDuration()));
    }

    public void appendEmbedWithNext(AudioTrack track, int queueSize){
        embedBuilder.addField("Next Track:", track.getInfo().title, true);
        embedBuilder.addField("Queue Size:", Integer.toString(queueSize), true);
    }

    public Collection<Button> getActionRowComponents(boolean isLoop){

        Collection<Button> components = new ArrayList<>();
        Button playPauseBtn;
        Button isLoopedBtn;
        Button skipBtn = Button.secondary("SKIP_BUTTON", Emoji.fromFormatted("<:rightarrow:1240691039992418426>"));
        //Button linkBtn = Button.secondary(track.getInfo().uri, Emoji.fromFormatted("<:chain:1240691034661453874>"));
        Button queueBtn = Button.secondary("QUEUE_BUTTON", Emoji.fromFormatted("<:playlistblue:1240727543385165859>"));
        Button shutBtn = Button.secondary("SHUTDOWN_BUTTON", Emoji.fromFormatted("<:powerbutton:1240690971277004881>"));

        if(player.isPaused())
            playPauseBtn = Button.secondary("RESUME_BUTTON", Emoji.fromFormatted("<:play:1240691041989033984>"));
        else
            playPauseBtn = Button.secondary("PAUSE_BUTTON", Emoji.fromFormatted("<:pause:1240691043733602384>"));

        if(isLoop)
            isLoopedBtn = Button.primary("LOOP_BUTTON", Emoji.fromFormatted("<:loopsong:1240691038348378222>"));
        else
            isLoopedBtn = Button.secondary("LOOP_BUTTON", Emoji.fromFormatted("<:loopsong:1240691038348378222>"));

        components.add(playPauseBtn);
        components.add(skipBtn);
        components.add(isLoopedBtn);
        //components.add(linkBtn);
        components.add(queueBtn);
        components.add(shutBtn);

        return components;
    }

    public MessageEmbed getEmbed(){
        return embedBuilder.build();
    }

    private String getTimeDuration(long length){
        int hour = (int)Math.floor(length / 3600000);
        int minute = (int)Math.floor(length % 3600000 / 60000);
        int second = (int)Math.floor(length % 3600000 % 60000 / 1000);
        return (hour == 0) ? String.format("%02d:%02d",minute,second) : String.format("%02d:%02d:%02d",hour,minute,second);
    }
}
