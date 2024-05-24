package me.korolz.rocketbot.commands;

import jakarta.annotation.PostConstruct;
import me.korolz.rocketbot.helpers.PresenceChecker;
import me.korolz.rocketbot.interfaces.RocketbotCommand;
import me.korolz.rocketbot.lavaplayer.PlayerManager;
import me.korolz.rocketbot.listeners.CommandManager;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

@Component
public class Play implements RocketbotCommand {

    private final CommandManager commandManager;
    private final PlayerManager playerManager;

    //для реализации postconstruct нам надо добавить зависимость CommandManager
    @Autowired
    public Play(PlayerManager playerManager, CommandManager commandManager) {
        this.playerManager = playerManager;
        this.commandManager = commandManager;
    }

    @PostConstruct
    public void init() {
        commandManager.addCommand(this); //вместо ручного добавления в BotConfig
    }

    @Override
    public String getName() {
        return "p";
    }

    @Override
    public String getDescription() {
        return "Will add a song to a queue";
    }

    @Override
    public List<OptionData> getOptions() {
        List<OptionData> options = new ArrayList<>();
        options.add(new OptionData(OptionType.STRING, "search", "Name or link to the song", true));
        return options;
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
        else {
            Member member = event.getMember();
            GuildVoiceState memberVoiceState = member.getVoiceState();
            event.getGuild().getAudioManager().openAudioConnection(memberVoiceState.getChannel());
        }

        String link = event.getOption("search").getAsString();

        try {
            new URI(link);
            event.reply("**Requested**: " + link).addActionRow(
                    Button.secondary("APPEND_SONG", Emoji.fromFormatted("<:send:1240690932504854599>")),
                    Button.secondary(link, Emoji.fromFormatted("<:chain:1240691034661453874>")).asDisabled()).queue();
            playerManager.play(event.getGuild(), link);
        } catch (URISyntaxException e) {
            String name = link;
            link = "ytsearch:" + link;
            event.reply("**Searching**: " + name).addActionRow(
                    Button.secondary("APPEND_SONG", Emoji.fromFormatted("<:send:1240690932504854599>")),
                    Button.secondary(link, Emoji.fromFormatted("<:chain:1240691034661453874>")).asDisabled()).queue();
            playerManager.play(event.getGuild(), link);
        }
    }
}
