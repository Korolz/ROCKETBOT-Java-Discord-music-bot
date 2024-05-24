package me.korolz.rocketbot.lavaplayer;

import com.github.topi314.lavasrc.spotify.SpotifySourceManager;
import com.github.topi314.lavasrc.yandexmusic.YandexMusicSourceManager;
import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import com.sedmelluq.discord.lavaplayer.source.http.HttpAudioSourceManager;
import com.sedmelluq.discord.lavaplayer.source.soundcloud.SoundCloudAudioSourceManager;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import dev.lavalink.youtube.YoutubeAudioSourceManager;
import dev.lavalink.youtube.clients.AndroidWithThumbnail;
import dev.lavalink.youtube.clients.MusicWithThumbnail;
import dev.lavalink.youtube.clients.WebWithThumbnail;
import dev.lavalink.youtube.clients.skeleton.Client;
import net.dv8tion.jda.api.entities.Guild;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class PlayerManager {

    //private static PlayerManager INSTANCE;
    private final Map<Long, GuildMusicManager> guildMusicManagers = new HashMap<>();
    private final AudioPlayerManager audioPlayerManager = new DefaultAudioPlayerManager();

    @Autowired
    private PlayerManager(
            @Value("${bot.spotify.secret}") String spotifyClientSecret,
            @Value("${bot.spotify.id}") String spotifyClientId,
            @Value("${bot.yandex.accessToken}") String yandexAccessToken
    ) {

        audioPlayerManager.registerSourceManager(new SpotifySourceManager(null, spotifyClientId, spotifyClientSecret, null, audioPlayerManager));
        audioPlayerManager.registerSourceManager(new YoutubeAudioSourceManager(/*allowSearch:*/ true, new Client[] { new MusicWithThumbnail(), new WebWithThumbnail(), new AndroidWithThumbnail() }));
        audioPlayerManager.registerSourceManager(new YandexMusicSourceManager(yandexAccessToken));
        audioPlayerManager.registerSourceManager(new HttpAudioSourceManager());
        audioPlayerManager.registerSourceManager(SoundCloudAudioSourceManager.createDefault());
    }

//    public static PlayerManager get() {
//        if(INSTANCE == null) {
//            INSTANCE = new PlayerManager();
//        }
//        return INSTANCE;
//    }

    public GuildMusicManager getGuildMusicManager(Guild guild) {
        return guildMusicManagers.computeIfAbsent(guild.getIdLong(), (guildId) -> {
            GuildMusicManager musicManager = new GuildMusicManager(audioPlayerManager, guild);
            guild.getAudioManager().setSendingHandler(musicManager.getAudioForwarder());
            return musicManager;
        });
    }

    public void play(Guild guild, String trackURL) {
        GuildMusicManager guildMusicManager = getGuildMusicManager(guild);
        audioPlayerManager.loadItemOrdered(guildMusicManager, trackURL, new AudioLoadResultHandler() {
            @Override
            public void trackLoaded(AudioTrack track) {
                guildMusicManager.getTrackScheduler().queue(track);
            }

            @Override
            public void playlistLoaded(AudioPlaylist playlist) {
                if(playlist.isSearchResult())
                    guildMusicManager.getTrackScheduler().queue(playlist.getTracks().getFirst());
                else {
                    for (AudioTrack track : playlist.getTracks())
                        guildMusicManager.getTrackScheduler().queue(track);
                }
            }

            @Override
            public void noMatches() {
                guildMusicManager.getTrackScheduler().getMusicTextChannel().sendMessage("There is no matches to this track").queue();
            }

            @Override
            public void loadFailed(FriendlyException exception) {
                if(exception.severity.equals(FriendlyException.Severity.COMMON)) {
                    guildMusicManager.getTrackScheduler().getMusicTextChannel().sendMessage("Unable to load this track :(").queue();
                    exception.printStackTrace();
                }
                else
                    guildMusicManager.getTrackScheduler().getMusicTextChannel().sendMessage("Track failed to load properly").queue();
            }
        });
    }
}
