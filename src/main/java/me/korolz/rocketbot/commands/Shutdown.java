package me.korolz.rocketbot.commands;

import jakarta.annotation.PostConstruct;
import me.korolz.rocketbot.helpers.PresenceChecker;
import me.korolz.rocketbot.interfaces.RocketbotCommand;
import me.korolz.rocketbot.lavaplayer.GuildMusicManager;
import me.korolz.rocketbot.lavaplayer.TrackScheduler;
import me.korolz.rocketbot.lavaplayer.PlayerManager;
import me.korolz.rocketbot.listeners.CommandManager;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.List;

@Component
public class Shutdown implements RocketbotCommand {

    private final CommandManager commandManager;
    private final PlayerManager playerManager;

    //для реализации postconstruct нам надо добавить зависимость CommandManager
    @Autowired
    public Shutdown(PlayerManager playerManager, CommandManager commandManager) {
        this.playerManager = playerManager;
        this.commandManager = commandManager;
    }
    @PostConstruct
    public void init() {
        commandManager.addCommand(this); //вместо ручного добавления в BotConfig
    }
    @Override
    public String getName() {
        return "shut";
    }

    @Override
    public String getDescription() {
        return "Full Bot Shutdown";
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
        TrackScheduler trackScheduler = guildMusicManager.getTrackScheduler();
        trackScheduler.getQueue().clear();
        if(trackScheduler.isLoop())
            trackScheduler.toggleLoop();
        trackScheduler.getPlayer().destroy();
        event.reply("**See you next time!**").setEphemeral(true).queue();
        event.getGuild().getAudioManager().closeAudioConnection();
    }
}
