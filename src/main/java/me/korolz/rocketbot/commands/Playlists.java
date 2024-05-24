package me.korolz.rocketbot.commands;

import jakarta.annotation.PostConstruct;
import me.korolz.rocketbot.interfaces.RocketbotCommand;
import me.korolz.rocketbot.helpers.YoutubeHandler;
import me.korolz.rocketbot.lavaplayer.PlayerManager;
import me.korolz.rocketbot.listeners.CommandManager;
import me.korolz.rocketbot.structures.PlaylistsSelectMenu;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.security.GeneralSecurityException;

@Component
public class Playlists implements RocketbotCommand {

    @Value("${spring.application.name}")
    private String botName;
    private final CommandManager commandManager;
    private final YoutubeHandler youtubeHandler;

    //для реализации postconstruct нам надо добавить зависимость CommandManager
    @Autowired
    public Playlists(CommandManager commandManager, YoutubeHandler youtubeHandler) {
        this.commandManager = commandManager;
        this.youtubeHandler = youtubeHandler;
    }

    @PostConstruct
    public void init() {
        commandManager.addCommand(this); //вместо ручного добавления в BotConfig
    }

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
            PlaylistsSelectMenu message = new PlaylistsSelectMenu(youtubeHandler.getPlaylists());
            event.reply("Choose "+botName+" playlist to listen:").addActionRow(message.getPlaylistsAsSelectMenu()).setEphemeral(true).queue();
        } catch (IOException | GeneralSecurityException e) {
            event.reply("I cannot get access to my playlists, please tell Admin about it").setEphemeral(true).queue();
        }
    }
}
