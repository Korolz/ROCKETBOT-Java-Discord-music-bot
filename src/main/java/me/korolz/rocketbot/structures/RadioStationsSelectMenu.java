package me.korolz.rocketbot.structures;

import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.interactions.components.selections.StringSelectMenu;

public class RadioStationsSelectMenu {
    public static StringSelectMenu getRadioAsSelectMenu(){

        StringSelectMenu.Builder builder = StringSelectMenu.create("radios");

        builder.addOption("Dorozhnoe Radio", "https://dorognoe.hostingradio.ru:8000/dorognoe", Emoji.fromUnicode("U+1F6DE"));
        builder.addOption("Radio Energy", "https://pub0201.101.ru:8000/stream/air/aac/64/99", Emoji.fromUnicode("U+26A1"));
        builder.addOption("Radio Chanson", "https://chanson.hostingradio.ru:8041/chanson128.mp3", Emoji.fromUnicode("U+1F451"));
        builder.addOption("Baba Radio(TR)", "http://37.247.98.7/;stream.mp3", Emoji.fromUnicode("\uD83C\uDDF9\uD83C\uDDF7"));
        builder.addOption("Hentai Radio(DE)", "https://hentairadio.stream.laut.fm/hentai_radio", Emoji.fromUnicode("U+1F480"));
        builder.addOption("Radio Paradisagasy(MG)", "https://stream.deevaradio.net:10443/paradisagasy", Emoji.fromUnicode("\uD83C\uDDF2\uD83C\uDDEC"));

        return builder.build();
    }
}
