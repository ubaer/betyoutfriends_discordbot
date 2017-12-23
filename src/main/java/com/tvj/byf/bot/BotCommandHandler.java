package com.tvj.byf.bot;

import com.tvj.byf.controller.BetController;
import com.tvj.byf.converter.DiscordObjectConverter;
import com.tvj.byf.domain.OpenBet;
import com.tvj.byf.domain.User;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;

public class BotCommandHandler {
    public void help() {

    }

    public void startOpenBet(MessageReceivedEvent event) {
        User author = DiscordObjectConverter.discorduserToUser(event.getAuthor());
        OpenBet bet = BetController.startOpenBet(author, "Temp test title");
        //todo iets van opslaan van de bet
        BotUtils.sendMessage(event.getChannel(), "Bet \'"+bet.getTitle()+"\' created by:" + bet.getCreater().getUsername() + "To modify this bet please use the ID"+ bet.getId());
    }
}
