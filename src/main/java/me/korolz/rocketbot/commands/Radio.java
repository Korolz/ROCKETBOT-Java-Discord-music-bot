package me.korolz.rocketbot.commands;

import jakarta.annotation.PostConstruct;
import me.korolz.rocketbot.interfaces.RocketbotCommand;
import me.korolz.rocketbot.listeners.CommandManager;
import me.korolz.rocketbot.structures.RadioStationsSelectMenu;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.*;

@Component
public class Radio implements RocketbotCommand {

    private final CommandManager commandManager;

    //для реализации postconstruct нам надо добавить зависимость CommandManager
    @Autowired
    public Radio(CommandManager commandManager) {
        this.commandManager = commandManager;
    }

    @PostConstruct
    public void init() {
        commandManager.addCommand(this); //вместо ручного добавления в BotConfig
    }
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
