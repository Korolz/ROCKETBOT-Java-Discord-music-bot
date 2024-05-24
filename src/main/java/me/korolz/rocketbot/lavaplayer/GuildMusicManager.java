package me.korolz.rocketbot.lavaplayer;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import lombok.Getter;
import net.dv8tion.jda.api.entities.Guild;

@Getter
public class GuildMusicManager {

    private final TrackScheduler trackScheduler;
    private final AudioForwarder audioForwarder;

    public GuildMusicManager(AudioPlayerManager manager, Guild guild) {

        AudioPlayer player = manager.createPlayer();
        trackScheduler = new TrackScheduler(player, guild);
        player.addListener(trackScheduler);
        audioForwarder = new AudioForwarder(player, guild);
    }
}