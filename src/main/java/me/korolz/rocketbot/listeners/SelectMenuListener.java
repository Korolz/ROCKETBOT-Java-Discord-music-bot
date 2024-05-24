package me.korolz.rocketbot.listeners;

import com.google.api.services.youtube.model.PlaylistItem;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import me.korolz.rocketbot.helpers.PresenceChecker;
import me.korolz.rocketbot.helpers.YoutubeHandler;
import me.korolz.rocketbot.lavaplayer.PlayerManager;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.interaction.component.StringSelectInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.List;

@Component
public class SelectMenuListener extends ListenerAdapter {

    private PlayerManager playerManager;
    private YoutubeHandler youtubeHandler;
    @Autowired
    public SelectMenuListener(PlayerManager playerManager, YoutubeHandler youtubeHandler) {
        this.playerManager = playerManager;
        this.youtubeHandler = youtubeHandler;
    }

    @Override
    public void onStringSelectInteraction(StringSelectInteractionEvent event) {

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
        else {
            Member member = event.getMember();
            GuildVoiceState memberVoiceState = member.getVoiceState();
            event.getGuild().getAudioManager().openAudioConnection(memberVoiceState.getChannel());
        }

        if (event.getComponentId().equals("playlists")) {
            String playlistId = event.getValues().getFirst();
            List<PlaylistItem> tracks = null;
            try {
                tracks = youtubeHandler.getPlaylistItems(playlistId);
            } catch (IOException | GeneralSecurityException e) {
                event.reply("I cannot get access to playlist https://youtube.com/playlist?list=" +playlistId+ " please tell Admin about it").setEphemeral(true).queue();
            }

            //shuffling and converting objects to strings(link parts)
            Collections.shuffle(tracks);
            List<String> trackIds = tracks.stream()
                    .map(track -> track.getContentDetails().getVideoId())
                    .toList();

            try {
                for(String trackId : trackIds){
                    playerManager.play(event.getGuild(), "https://www.youtube.com/watch?v="+trackId);
                }
            }
            catch(FriendlyException e) {
                event.reply("Ya obosralsa...").queue();
                return;
            }

            event.reply("RocketPlaylist loaded").queue();
        }

        if (event.getComponentId().equals("radios")){ //IN DEVELOPMENT (WAITING FOR AACP SUPPORT)

            String radioId = event.getValues().getFirst();
            playerManager.play(event.getGuild(), radioId);
            event.reply("Radio connected").queue();
        }
    }
}

