package me.korolz.rocketbot.helpers;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.Playlist;
import com.google.api.services.youtube.model.PlaylistItem;
import com.google.api.services.youtube.model.PlaylistItemListResponse;
import com.google.api.services.youtube.model.PlaylistListResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.List;

@Component
public class YoutubeHandler {

    private String APPLICATION_NAME;
    private static final GsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();
    private String apiKey;
    private String channelId;

    @Autowired
    public YoutubeHandler(
           @Value("${spring.application.name}") String name,
           @Value("${bot.youtube.apiToken}") String token,
           @Value("${bot.youtube.channelId}") String id) {
       this.APPLICATION_NAME = name;
       this.channelId = id;
       this.apiKey = token;
   }

    public java.util.List<Playlist> getPlaylists() throws IOException, GeneralSecurityException {

        YouTube youtube = new YouTube.Builder(
                GoogleNetHttpTransport.newTrustedTransport(),
                JSON_FACTORY,
                null)
                .setApplicationName(APPLICATION_NAME)
                .build();

        YouTube.Playlists.List playlistRequest = youtube.playlists()
                .list("snippet,contentDetails")
                .setKey(apiKey)
                .setMaxResults(25L)
                .setChannelId(channelId);

        PlaylistListResponse playlistListResponse = playlistRequest.execute();

        return playlistListResponse.getItems();
    }

    public List<PlaylistItem> getPlaylistItems(String playlistId) throws IOException, GeneralSecurityException {

        YouTube youtube = new YouTube.Builder(
                GoogleNetHttpTransport.newTrustedTransport(),
                JSON_FACTORY,
                null)
                .setApplicationName(APPLICATION_NAME)
                .build();

        YouTube.PlaylistItems.List playlistItemsRequest = youtube.playlistItems()
                .list("contentDetails")
                .setKey(apiKey)
                .setMaxResults(50L)
                .setPlaylistId(playlistId);

        PlaylistItemListResponse playlistItemListResponse = playlistItemsRequest.execute();

        return playlistItemListResponse.getItems();
    }
}
