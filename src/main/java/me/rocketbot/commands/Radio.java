package me.rocketbot.commands;

import me.rocketbot.interfaces.RocketBotCommand;
import me.rocketbot.structures.RadioStationsSelectMenu;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import java.util.*;

public class Radio implements RocketBotCommand {
    @Override
    public String getName() {
        return "radio";
    }

    @Override
    public String getDescription() {
        return "Lists connected Radiostations";
    }

    @Override
    public List<OptionData> getOptions() {
        return null;
    }

    @Override
    public void execute(SlashCommandInteractionEvent event){
        event.reply("Choose radio:").addActionRow(RadioStationsSelectMenu.getRadioAsSelectMenu()).setEphemeral(true).queue();
    }
}
