package me.rocketbot.listeners;

import com.google.api.services.youtube.model.PlaylistItem;
import me.rocketbot.helpers.YoutubeHandler;
import me.rocketbot.lavaplayer.PlayerManager;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.interaction.component.StringSelectInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.List;

public class SelectMenuListener extends ListenerAdapter {
    @Override
    public void onStringSelectInteraction(StringSelectInteractionEvent event) {

        if (event.getComponentId().equals("playlists")) {
            Member member = event.getMember();
            GuildVoiceState memberVoiceState = member.getVoiceState();

            if(!memberVoiceState.inAudioChannel()) { //checks presence of a member
                event.reply("You need to be in a voice channel").setEphemeral(true).queue();
                return;
            }

            Member self = event.getGuild().getSelfMember();
            GuildVoiceState selfVoiceState = self.getVoiceState();

            if(!selfVoiceState.inAudioChannel()) { //checks presence of a member in a same channel
                event.getGuild().getAudioManager().openAudioConnection(memberVoiceState.getChannel());
            } else {
                if(selfVoiceState.getChannel() != memberVoiceState.getChannel()) {
                    event.reply("You are not in the same channel as me").setEphemeral(true).queue();
                    return;
                }
            }

            //preparing List of TrackObjects
            PlayerManager playerManager = PlayerManager.get();
            String playlistId = event.getValues().get(0);
            List<PlaylistItem> tracks = null;
            try {
                tracks = YoutubeHandler.getPlaylistItems(playlistId);
            } catch (IOException | GeneralSecurityException e) {
                event.reply("I cannot get access to playlist https://youtube.com/playlist?list=" +playlistId+ " please tell Roman about it").setEphemeral(true).queue();
            }

            //shuffling and converting objects to strings(link parts)
            Collections.shuffle(tracks);
            List<String> trackIds = tracks.stream()
                    .map(track -> track.getContentDetails().getVideoId())
                    .toList();

            for(String trackId : trackIds){
                playerManager.play(event.getGuild(), "https://www.youtube.com/watch?v="+trackId);
            }

            event.reply("RocketPlaylist loaded").queue();
        }

        if (event.getComponentId().equals("radios")){ //IN DEVELOPMENT (WAITING FOR AACP SUPPORT)
            Member member = event.getMember();
            GuildVoiceState memberVoiceState = member.getVoiceState();

            if(!memberVoiceState.inAudioChannel()) { //checks presence of a member
                event.reply("You need to be in a voice channel").setEphemeral(true).queue();
                return;
            }

            Member self = event.getGuild().getSelfMember();
            GuildVoiceState selfVoiceState = self.getVoiceState();

            if(!selfVoiceState.inAudioChannel()) { //checks presence of a member in a same channel
                event.getGuild().getAudioManager().openAudioConnection(memberVoiceState.getChannel());
            } else {
                if(selfVoiceState.getChannel() != memberVoiceState.getChannel()) {
                    event.reply("You are not in the same channel as me").setEphemeral(true).queue();
                    return;
                }
            }

            PlayerManager playerManager = PlayerManager.get();
            String radioId = event.getValues().get(0);
            playerManager.play(event.getGuild(), radioId);
            event.reply("Radio connected").queue();
        }
    }
}
