package me.rocketbot.helpers;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.Playlist;
import com.google.api.services.youtube.model.PlaylistItem;
import com.google.api.services.youtube.model.PlaylistItemListResponse;
import com.google.api.services.youtube.model.PlaylistListResponse;
import io.github.cdimascio.dotenv.Dotenv;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.List;

public class YoutubeHandler {
    private static final String APPLICATION_NAME = "ROCKETBOT";
    private static final GsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();
    private static final String apiKey = Dotenv.load().get("YT_API_KEY");
    private static final String channelId = "UC2yvak2D52_HhhxXHzT4MDA";

    public static java.util.List<Playlist> getPlaylists() throws IOException, GeneralSecurityException {

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

    public static List<PlaylistItem> getPlaylistItems(String playlistId) throws IOException, GeneralSecurityException {

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