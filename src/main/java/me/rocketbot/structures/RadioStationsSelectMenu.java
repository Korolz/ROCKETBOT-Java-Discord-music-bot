package me.rocketbot.structures;

import net.dv8tion.jda.api.interactions.components.selections.StringSelectMenu;

import java.util.Map;

public class RadioStationsSelectMenu {
    private static final Map<String,String> radiostations = Map.of(
            "Dorozhnoe Radio", "https://dorognoe.hostingradio.ru:8000/dorognoe",
            "Radio Energy", "https://pub0201.101.ru:8000/stream/air/aac/64/99",
            "EDM Radio", "https://lk.castnow.ru:8100/edm-320.mp3"
    );

    public static StringSelectMenu getRadioAsSelectMenu(){

        StringSelectMenu.Builder builder = StringSelectMenu.create("radios");
        for(Map.Entry<String,String> entry : radiostations.entrySet()){
            builder.addOption(entry.getKey(), entry.getValue());
        }

        return builder.build();
    }
}
