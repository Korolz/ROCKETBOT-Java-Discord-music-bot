package me.rocketbot.commands;

import me.rocketbot.interfaces.RocketBotCommand;
import me.rocketbot.helpers.YoutubeHandler;
import me.rocketbot.structures.PlaylistsSelectMenu;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import java.io.IOException;
import java.security.GeneralSecurityException;


public class Playlists implements RocketBotCommand {
    @Override
    public String getName() {
        return "playlists";
    }

    @Override
    public String getDescription() {
        return "Lists all RocketBot Playlists";
    }

    @Override
    public java.util.List<OptionData> getOptions() {
        return null;
    }

    @Override
    public void execute(SlashCommandInteractionEvent event) {
        try {
            PlaylistsSelectMenu message = new PlaylistsSelectMenu(YoutubeHandler.getPlaylists());
            event.reply("Choose ROCKETBOT playlist to listen:").addActionRow(message.getPlaylistsAsSelectMenu()).setEphemeral(true).queue();
        } catch (IOException | GeneralSecurityException e) {
            event.reply("I cannot get access to my playlists, please tell Roman about it").setEphemeral(true).queue();
        }
    }
}
