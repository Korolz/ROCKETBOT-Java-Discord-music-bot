package me.rocketbot.structures;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.buttons.Button;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collection;

public class NowPlayingMessage {
    private final AudioPlayer player;
    private final AudioTrack track;
    private final EmbedBuilder embedBuilder = new EmbedBuilder();

    public NowPlayingMessage(AudioPlayer player, AudioTrack track){
        this.player = player;
        this.track = track;
    }

    public void createEmbed(){
        embedBuilder.setColor(11766599);
//        embedBuilder.setAuthor("ROCKETBOT");
        embedBuilder.setTitle("*Now Playing...*");
        embedBuilder.addField(track.getInfo().title, track.getInfo().author, true);
//        embedBuilder.addField("Source of track:", "`" + track.getInfo().uri + "`", true);
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
        Button skipBtn = Button.primary("SKIP_BUTTON", Emoji.fromUnicode("U+23ED"));
        Button linkBtn = Button.link(track.getInfo().uri, "Link");
        Button shutBtn = Button.danger("SHUTDOWN_BUTTON", Emoji.fromUnicode("U+1F4A5"));

        if(player.isPaused())
            playPauseBtn = Button.primary("RESUME_BUTTON", Emoji.fromUnicode("U+25B6"));
        else
            playPauseBtn = Button.primary("PAUSE_BUTTON", Emoji.fromUnicode("U+23F8"));

        if(isLoop)
            isLoopedBtn = Button.primary("LOOP_BUTTON", Emoji.fromUnicode("U+1F502"));
        else
            isLoopedBtn = Button.secondary("LOOP_BUTTON", Emoji.fromUnicode("U+1F502"));

        components.add(playPauseBtn);
        components.add(skipBtn);
        components.add(isLoopedBtn);
        components.add(linkBtn);
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
