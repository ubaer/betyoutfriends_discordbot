package com.tvj.byf.bot;

import sx.blah.discord.api.events.EventSubscriber;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;


public class BotEvents {
    BotCommandHandler commandHandler = new BotCommandHandler();

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
                break;
            case "$StartOpenbet":
                commandHandler.startOpenBet(event);
                break;
            case "$stopbet":
                BotUtils.sendMessage(event.getChannel(), "stopbet command received");
                break;
            default:
                BotUtils.sendMessage(event.getChannel(), "are you retarded? " + command + " isn\'t a command");
                break;
        }

    }
}
