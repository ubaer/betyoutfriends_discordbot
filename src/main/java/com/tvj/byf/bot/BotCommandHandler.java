package com.tvj.byf.bot;

import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;

public class BotCommandHandler {
    public void help() {

    }

    public void startOpenBet(MessageReceivedEvent event) {

        BotUtils.sendMessage(event.getChannel(), "startbet command received " + event.getAuthor().getName() + event.getAuthor().getDiscriminator());
    }
}
