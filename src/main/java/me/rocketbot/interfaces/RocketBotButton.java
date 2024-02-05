package me.rocketbot.interfaces;

import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;

public interface RocketBotButton {
    //interface of all Buttons
    String getId();
    void execute(ButtonInteractionEvent event);
}
