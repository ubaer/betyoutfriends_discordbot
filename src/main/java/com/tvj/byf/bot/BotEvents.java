package com.tvj.byf.bot;

import com.tvj.byf.service.BetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sx.blah.discord.api.events.EventSubscriber;
import sx.blah.discord.handle.impl.events.ReadyEvent;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageSendEvent;
import sx.blah.discord.handle.impl.events.guild.channel.message.reaction.ReactionAddEvent;
import sx.blah.discord.handle.impl.events.guild.channel.message.reaction.ReactionRemoveEvent;
import sx.blah.discord.handle.obj.IMessage;

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

        switch (command) {
            case "$help":
                commandHandler.help(event);
                break;
            case "$Y/N":
                commandHandler.createYesNoBet(event);
                break;
            case "$Set":
                commandHandler.configureBet(event);
                break;
            default:
                BotUtils.sendMessageAsynchrone(event.getChannel(), "are you retarded? " + command + " isn\'t a command");
                break;
        }
    }
}
