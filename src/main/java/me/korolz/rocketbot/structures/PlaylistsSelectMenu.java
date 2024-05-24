package me.korolz.rocketbot.structures;

import com.google.api.services.youtube.model.Playlist;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.entities.emoji.UnicodeEmoji;
import net.dv8tion.jda.api.interactions.components.selections.StringSelectMenu;
import java.util.List;
import java.util.Optional;

public class PlaylistsSelectMenu {
    private final List<Playlist> playlists;
    public PlaylistsSelectMenu(List<Playlist> playlists){
        this.playlists = playlists;
    }

    public StringSelectMenu getPlaylistsAsSelectMenu(){

        StringSelectMenu.Builder builder = StringSelectMenu.create("playlists");
        for(Playlist playlist : playlists){
            Optional<UnicodeEmoji> unicodePlaylistEmoji = Optional.of(Emoji.fromUnicode(playlist.getSnippet().getDescription()));
            builder.addOption(
                    playlist.getSnippet().getTitle(),
                    playlist.getId(),
                    "Tracks: "+playlist.getContentDetails().getItemCount(),
                    unicodePlaylistEmoji.orElse(Emoji.fromUnicode("U+1F3B5"))
            );
        }

        return builder.build();
    }
}
