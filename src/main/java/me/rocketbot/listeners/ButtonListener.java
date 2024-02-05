package me.rocketbot.listeners;

import me.rocketbot.interfaces.RocketBotButton;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.util.ArrayList;
import java.util.List;

public class ButtonListener extends ListenerAdapter {
    private List<RocketBotButton> buttons = new ArrayList<>();
    @Override
    public void onButtonInteraction(ButtonInteractionEvent event) {
        for(RocketBotButton button : buttons){
            if(button.getId().equals(event.getButton().getId())) {
                button.execute(event);
                return;
            }
        }
    }
    public void add(RocketBotButton button) {
    buttons.add(button);
}
}