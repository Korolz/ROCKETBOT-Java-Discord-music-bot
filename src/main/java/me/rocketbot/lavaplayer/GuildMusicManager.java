package me.rocketbot.lavaplayer;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import net.dv8tion.jda.api.entities.Guild;

public class GuildMusicManager {

    private TrackScheduler trackScheduler;
    private AudioForwarder audioForwarder;

    public TrackScheduler getTrackScheduler() {
        return trackScheduler;
    }

    public AudioForwarder getAudioForwarder() {
        return audioForwarder;
    }

    public GuildMusicManager(AudioPlayerManager manager, Guild guild) {

        AudioPlayer player = manager.createPlayer();
        trackScheduler = new TrackScheduler(player, guild);
        player.addListener(trackScheduler);
        audioForwarder = new AudioForwarder(player, guild);


    }
}
