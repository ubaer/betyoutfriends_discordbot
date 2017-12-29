package com.tvj.byf.bot;

import com.tvj.byf.service.BetService;
import com.tvj.byf.converter.DiscordObjectManager;
import com.tvj.byf.domain.OpenBet;
import com.tvj.byf.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;

@Service
@Transactional
class BotCommandHandler {

    private final DiscordObjectManager discordObjectManager;

    private final BetService betService;

    @Autowired
    public BotCommandHandler(DiscordObjectManager discordObjectManager, BetService betController) {
        this.discordObjectManager = discordObjectManager;
        this.betService = betController;
    }

    void help(MessageReceivedEvent event) {

    }

    void createOpenBet(MessageReceivedEvent event) {
        String betTitle = event.getMessage().getContent().substring(14);
        User author = discordObjectManager.discorduserToUser(event.getAuthor());
        OpenBet bet = betService.startOpenBet(author, betTitle);
        BotUtils.sendMessage(event.getChannel(), "Bet \'" + bet.getTitle() + "\' created by:" + bet.getCreater().getUsername() + "To modify this bet please use the ID" + bet.getId());
    }
}
