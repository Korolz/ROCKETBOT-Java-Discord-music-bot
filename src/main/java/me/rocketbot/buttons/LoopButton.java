package me.rocketbot.buttons;

import me.rocketbot.interfaces.RocketBotButton;
import me.rocketbot.lavaplayer.GuildMusicManager;
import me.rocketbot.lavaplayer.PlayerManager;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;

public class LoopButton implements RocketBotButton {
    @Override
    public String getId() {
        return "LOOP_BUTTON";
    }

    @Override
    public void execute(ButtonInteractionEvent event) {
        GuildMusicManager guildMusicManager = PlayerManager.get().getGuildMusicManager(event.getGuild());
        guildMusicManager.getTrackScheduler().toggleLoop();
        event.reply("**Repeat** is now " + guildMusicManager.getTrackScheduler().isLoop()).setEphemeral(true).queue();
    }
}
