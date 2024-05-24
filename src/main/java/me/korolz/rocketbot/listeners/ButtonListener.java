package me.korolz.rocketbot.listeners;

import me.korolz.rocketbot.interfaces.RocketbotButton;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.springframework.stereotype.Component;
import java.util.ArrayList;
import java.util.List;

@Component
public class ButtonListener extends ListenerAdapter {
    private final List<RocketbotButton> buttons = new ArrayList<>();

    @Override
    public void onButtonInteraction(ButtonInteractionEvent event) {
        for (RocketbotButton button : buttons) {
            if (button.getId().equals(event.getButton().getId())) {
                button.execute(event);
                return;
            }
        }
    }

    public void addButton(RocketbotButton button) {
        buttons.add(button);
    }
}
