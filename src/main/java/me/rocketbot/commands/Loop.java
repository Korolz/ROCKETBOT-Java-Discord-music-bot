package me.rocketbot.commands;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import me.rocketbot.interfaces.RocketBotCommand;
import me.rocketbot.lavaplayer.GuildMusicManager;
import me.rocketbot.lavaplayer.PlayerManager;
import me.rocketbot.structures.NowPlayingMessage;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.interactions.components.ActionRow;

import java.util.List;

public class Loop implements RocketBotCommand {
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
        Member member = event.getMember();
        GuildVoiceState memberVoiceState = member.getVoiceState();

        if(!memberVoiceState.inAudioChannel()) {
            event.reply("You need to be in a voice channel").setEphemeral(true).queue();
            return;
        }

        Member self = event.getGuild().getSelfMember();
        GuildVoiceState selfVoiceState = self.getVoiceState();

        if(!selfVoiceState.inAudioChannel()) {
            event.reply("I am not in an audio channel").setEphemeral(true).queue();
            return;
        }

        if(selfVoiceState.getChannel() != memberVoiceState.getChannel()) {
            event.reply("You are not in the same channel as me").setEphemeral(true).queue();
            return;
        }

        GuildMusicManager guildMusicManager = PlayerManager.get().getGuildMusicManager(event.getGuild());
        guildMusicManager.getTrackScheduler().toggleLoop();
        AudioPlayer player = guildMusicManager.getTrackScheduler().getPlayer();

        NowPlayingMessage message = new NowPlayingMessage(player, player.getPlayingTrack());
        event.getChannel().editMessageComponentsById(
                guildMusicManager.getTrackScheduler().getMessageId(),
                ActionRow.of(message.getActionRowComponents(guildMusicManager.getTrackScheduler().isLoop()))
        ).queue();
        event.reply("**Repeat** is now " + guildMusicManager.getTrackScheduler().isLoop()).setEphemeral(true).queue();
    }
}