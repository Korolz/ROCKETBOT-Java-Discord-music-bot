package me.rocketbot.buttons;

import me.rocketbot.interfaces.RocketBotButton;
import me.rocketbot.lavaplayer.GuildMusicManager;
import me.rocketbot.lavaplayer.PlayerManager;
import me.rocketbot.lavaplayer.TrackScheduler;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;

public class ShutdownButton implements RocketBotButton {
    @Override
    public String getId() {
        return "SHUTDOWN_BUTTON";
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

        if(!selfVoiceState.inAudioChannel()) { //checks presence in a channel
            event.reply("I am not in an audio channel").setEphemeral(true).queue();
            return;
        }

        if(selfVoiceState.getChannel() != memberVoiceState.getChannel()) {
            event.reply("You are not in the same channel as me").setEphemeral(true).queue();
            return;
        }

        GuildMusicManager guildMusicManager = PlayerManager.get().getGuildMusicManager(event.getGuild());
        TrackScheduler trackScheduler = guildMusicManager.getTrackScheduler();
        trackScheduler.getQueue().clear();
        if(trackScheduler.isLoop())
            trackScheduler.toggleLoop();
        trackScheduler.getPlayer().destroy();
        event.reply("**Bye!**").setEphemeral(true).queue();
        event.getGuild().getAudioManager().closeAudioConnection();
    }
}
