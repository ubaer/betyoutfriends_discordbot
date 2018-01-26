package com.tvj.byf.bot;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sx.blah.discord.api.events.EventSubscriber;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.handle.impl.events.guild.channel.message.reaction.ReactionAddEvent;
import sx.blah.discord.handle.impl.events.guild.channel.message.reaction.ReactionRemoveEvent;

@Service
public class BotEvents {
    private final BotCommandHandler commandHandler;

    @Autowired
    public BotEvents(BotCommandHandler commandHandler, BotMessageCreator messageSender) {
        this.commandHandler = commandHandler;
    }

    @EventSubscriber
    public void onMessageReceived(MessageReceivedEvent event) {
        String message = event.getMessage().getContent();
        if (message.startsWith(BotUtils.BOT_PREFIX)) {
            handleMessage(message, event);
        }
    }

    @EventSubscriber
    public void onReactionAdd(ReactionAddEvent event) {
        // Check if the reaction is on a bot message
        if (!event.getAuthor().isBot()) {
            return;
        }

        // If there is only one user reacting its the bot itself creating the emotes.
        if (event.getReaction().getUsers().size() < 2) {
            return;
        }
        commandHandler.reactionAdded(event);
    }

    @EventSubscriber
    public void onReactionRemove(ReactionRemoveEvent event) {
        commandHandler.reactionRemoved(event);
    }

    private void handleMessage(String message, MessageReceivedEvent event) {
        String command = message.replaceAll(" .*", "");
        command = command.toLowerCase();
        String possibleNumber = command.replace("$", "");
        int betNumber = convertToInt(possibleNumber);

        if (betNumber > 0) {
            command = "$set";
        }

        Boolean commandExecuted = false;
        switch (command) {
            case "$help":
                commandHandler.help(event);
                break;
            case "$y/n?":
                commandHandler.helpYesNo(event);
                break;
            case "$y/n":
                commandExecuted = commandHandler.createYesNoBet(event);
                break;
            case "$set":
                commandExecuted = commandHandler.configureBet(betNumber, event);
                break;
            default:
                BotUtils.sendMessageAsynchrone(event.getChannel(),command + " isn\'t a command, use $help for information");
                break;
        }
        if (commandExecuted) {
            event.getMessage().delete();
        }
    }

    private int convertToInt(String number) {
        int converted = -1;
        try {
            converted = Integer.parseInt(number);
        } catch (NumberFormatException ignored) {

        }
        return converted;
    }
}
