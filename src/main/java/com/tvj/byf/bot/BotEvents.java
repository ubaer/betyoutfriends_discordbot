package com.tvj.byf.bot;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import sx.blah.discord.api.events.EventSubscriber;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;

@Service
public class BotEvents {
    private final BotCommandHandler commandHandler;

    @Autowired
    public BotEvents(BotCommandHandler commandHandler) {
        this.commandHandler = commandHandler;
    }

    @EventSubscriber
    public void onMessageReceived(MessageReceivedEvent event) {
        String message = event.getMessage().getContent();
        if (message.startsWith(BotUtils.BOT_PREFIX)) {
            handleMessage(message, event);
        }
    }

    private void handleMessage(String message, MessageReceivedEvent event) {
        String command = message.replaceAll(" .*", "");

        switch (command) {
            case "$help":
                commandHandler.help(event);
                break;
            case "$StartOpenbet":
                commandHandler.createOpenBet(event);
                break;
            default:
                BotUtils.sendMessage(event.getChannel(), "are you retarded? " + command + " isn\'t a command");
                break;
        }
    }
}
