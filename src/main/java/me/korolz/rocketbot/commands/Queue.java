package me.korolz.rocketbot.commands;

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackInfo;
import jakarta.annotation.PostConstruct;
import me.korolz.rocketbot.helpers.PresenceChecker;
import me.korolz.rocketbot.interfaces.RocketbotCommand;
import me.korolz.rocketbot.lavaplayer.GuildMusicManager;
import me.korolz.rocketbot.lavaplayer.PlayerManager;
import me.korolz.rocketbot.listeners.CommandManager;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.ArrayList;
import java.util.List;

@Component
public class Queue implements RocketbotCommand {

    private final CommandManager commandManager;
    private final PlayerManager playerManager;

    //для реализации postconstruct нам надо добавить зависимость CommandManager
    @Autowired
    public Queue(PlayerManager playerManager, CommandManager commandManager) {
        this.playerManager = playerManager;
        this.commandManager = commandManager;
    }

    @PostConstruct
    public void init() {
        commandManager.addCommand(this); //вместо ручного добавления в BotConfig
    }
    @Override
    public String getName() {
        return "q";
    }

    @Override
    public String getDescription() {
        return "Shows queue of the player";
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
        List<AudioTrack> queue = new ArrayList<>(guildMusicManager.getTrackScheduler().getQueue());
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setColor(16121855);
        embedBuilder.setTitle("**Current Queue**");
        if(queue.isEmpty()) {
            embedBuilder.setDescription("Queue is empty");
        }
        for(int i = 0; i < queue.size() && i < 10; i++) {
            AudioTrackInfo info = queue.get(i).getInfo();
            embedBuilder.addField(i+1 + ":", info.title, false);
        }
        embedBuilder.addField("**Queue Size**:", String.valueOf(queue.size()),false);
        event.replyEmbeds(embedBuilder.build()).setEphemeral(true).queue();
    }
}
