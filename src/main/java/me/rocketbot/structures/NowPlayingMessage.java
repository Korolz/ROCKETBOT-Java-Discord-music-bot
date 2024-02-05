package me.rocketbot.structures;

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;

public class NowPlayingMessage {
    private final AudioTrack track;
    private final EmbedBuilder embedBuilder = new EmbedBuilder();

    public NowPlayingMessage(AudioTrack track){
        this.track = track;
    }

    public void createEmbed(){
        embedBuilder.setColor(11766599);
//        embedBuilder.setAuthor("ROCKETBOT");
        embedBuilder.setTitle("*Now Playing...*");
        embedBuilder.addField(track.getInfo().title, track.getInfo().author, true);
//        embedBuilder.addField("Source of track:", "`" + track.getInfo().uri + "`", true);
        embedBuilder.setThumbnail(track.getInfo().artworkUrl);
        try {
            embedBuilder.setFooter(this.getTimeDuration(track.getDuration()));
        } catch (IllegalArgumentException e) {
            embedBuilder.setFooter("♩ ♪ ♫ ♬");
        }
    }

    public void appendEmbedWithNext(AudioTrack track, int queueSize){
        embedBuilder.addField("Next Track:", track.getInfo().title, true);
        embedBuilder.addField("Queue Size:", Integer.toString(queueSize), true);
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
