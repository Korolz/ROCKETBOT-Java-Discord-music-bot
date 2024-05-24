package me.korolz.rocketbot.interfaces;

import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;

public interface RocketbotButton {
    String getId();
    void execute(ButtonInteractionEvent event);
}
