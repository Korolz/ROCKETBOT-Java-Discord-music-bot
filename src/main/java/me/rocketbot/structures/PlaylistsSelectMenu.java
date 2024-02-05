package me.rocketbot.structures;

import com.google.api.services.youtube.model.Playlist;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.interactions.components.selections.StringSelectMenu;

import java.util.List;

public class PlaylistsSelectMenu {
    private final List<Playlist> playlists;
    public PlaylistsSelectMenu(List<Playlist> playlists){
        this.playlists = playlists;
    }

    public StringSelectMenu getPlaylistsAsSelectMenu(){

        StringSelectMenu.Builder builder = StringSelectMenu.create("playlists");
        for(Playlist playlist : playlists){
            builder.addOption(
                    playlist.getSnippet().getTitle(),
//                    "https://youtube.com/playlist?list="+playlist.getId(),
                    playlist.getId(),
                    "Tracks: "+playlist.getContentDetails().getItemCount(),
                    Emoji.fromUnicode(playlist.getSnippet().getDescription())
            );
        }

        return builder.build();
    }
}
