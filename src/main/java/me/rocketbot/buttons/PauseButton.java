package me.rocketbot.buttons;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import me.rocketbot.interfaces.RocketBotButton;
import me.rocketbot.lavaplayer.GuildMusicManager;
import me.rocketbot.lavaplayer.PlayerManager;
import me.rocketbot.structures.NowPlayingMessage;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.interactions.components.ActionRow;

public class PauseButton implements RocketBotButton {
    @Override
    public String getId() {
        return "PAUSE_BUTTON";
    }

    @Override
    public void execute(ButtonInteractionEvent event) {
        Member member = event.getMember();
        GuildVoiceState memberVoiceState = member.getVoiceState();

        if(!memberVoiceState.inAudioChannel()) { //checks presence of a member
            event.reply("You need to be in a voice channel").setEphemeral(true).queue();
            return;
        }

        Member self = event.getGuild().getSelfMember();
        GuildVoiceState selfVoiceState = self.getVoiceState();

        if(selfVoiceState.getChannel() != memberVoiceState.getChannel()) {
            event.reply("You are not in the same channel as me").setEphemeral(true).queue();
            return;
        }

        GuildMusicManager guildMusicManager = PlayerManager.get().getGuildMusicManager(event.getGuild());
        guildMusicManager.getTrackScheduler().getPlayer().setPaused(true);

//        AudioPlayer player = guildMusicManager.getTrackScheduler().getPlayer();
//
//        NowPlayingMessage message = new NowPlayingMessage(player, player.getPlayingTrack());
//        event.getChannel().editMessageComponentsById(
//                guildMusicManager.getTrackScheduler().getMessageId(),
//                ActionRow.of(message.getActionRowComponents(guildMusicManager.getTrackScheduler().isLoop()))
//        ).queue();

        event.reply("**Paused**").setEphemeral(true).queue();
    }
}
