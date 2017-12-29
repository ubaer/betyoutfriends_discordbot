package com.tvj.byf;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import sx.blah.discord.api.IDiscordClient;
import com.tvj.byf.bot.*;

@SpringBootApplication
public class Main {
    private static BotEvents botEvents;

    @Autowired
    public Main(BotEvents botEvents){
        Main.botEvents = botEvents;
    }

    public static void main(String[] args) {

        SpringApplication.run(Main.class, args);


        if (args.length != 1) {
            System.out.println("Please enter the bots token as the first argument e.g java -jar thisjar.jar tokenhere");
            return;
        }

        IDiscordClient cli = BotUtils.getBuiltDiscordClient(args[0]);

        // Register a listener via the EventSubscriber annotation which allows for organisation and delegation of events
        cli.getDispatcher().registerListener(botEvents);

        // Only login after all events are registered otherwise some may be missed.
        cli.login();
    }
}