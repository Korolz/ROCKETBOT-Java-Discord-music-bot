package me.korolz.rocketbot.commands;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import jakarta.annotation.PostConstruct;
import me.korolz.rocketbot.helpers.PresenceChecker;
import me.korolz.rocketbot.interfaces.RocketbotCommand;
import me.korolz.rocketbot.lavaplayer.GuildMusicManager;
import me.korolz.rocketbot.lavaplayer.PlayerManager;
import me.korolz.rocketbot.listeners.CommandManager;
import me.korolz.rocketbot.structures.NowPlayingMessage;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class Loop implements RocketbotCommand {

    private final CommandManager commandManager;
    private final PlayerManager playerManager;

    //для реализации postconstruct нам надо добавить зависимость CommandManager
    @Autowired
    public Loop(PlayerManager playerManager, CommandManager commandManager) {
        this.playerManager = playerManager;
        this.commandManager = commandManager;
    }

    @PostConstruct
    public void init() {
        commandManager.addCommand(this); //вместо ручного добавления в BotConfig
    }
    @Override
    public String getName() {
        return "l";
    }

    @Override
    public String getDescription() {
        return "Will loop a song";
    }

    @Override
    public List<OptionData> getOptions() {
        return null;
    }

    @Override
    public void execute(SlashCommandInteractionEvent event) {
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

        GuildMusicManager guildMusicManager = playerManager.getGuildMusicManager(event.getGuild());
        guildMusicManager.getTrackScheduler().toggleLoop();
        AudioPlayer player = guildMusicManager.getTrackScheduler().getPlayer();

        NowPlayingMessage message = new NowPlayingMessage(player, player.getPlayingTrack());
        event.getChannel().editMessageComponentsById(
                guildMusicManager.getTrackScheduler().getMessageId(),
                ActionRow.of(message.getActionRowComponents(guildMusicManager.getTrackScheduler().isLoop()))
        ).queue();
        event.reply("**Loop** status: " + guildMusicManager.getTrackScheduler().isLoop()).setEphemeral(true).queue();
    }
}
