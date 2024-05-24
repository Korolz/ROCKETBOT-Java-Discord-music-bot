package me.korolz.rocketbot.config;

import me.korolz.rocketbot.listeners.ButtonListener;
import me.korolz.rocketbot.listeners.CommandManager;
import me.korolz.rocketbot.listeners.SelectMenuListener;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.requests.GatewayIntent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class RocketbotConfig {

    private JDA jda;

    @Autowired
    public RocketbotConfig(
            @Value("${bot.token}") String token,
            CommandManager commandManager,
            ButtonListener buttonListener,
            SelectMenuListener menuListener
    ) {
        JDABuilder jdaBuilder = JDABuilder.createDefault(token);
        jdaBuilder.setStatus(OnlineStatus.ONLINE)
                .enableIntents(
                        GatewayIntent.GUILD_MEMBERS,
                        GatewayIntent.GUILD_MESSAGES,
                        GatewayIntent.GUILD_PRESENCES
                );
        this.jda = jdaBuilder.build();

        jda.addEventListener(commandManager);
        jda.addEventListener(buttonListener);
        jda.addEventListener(menuListener);
    }
}
