package com.tvj.byf.bot;

import com.tvj.byf.domain.Bet;
import com.tvj.byf.domain.YesNoBet.YesNoBet;
import com.tvj.byf.domain.YesNoBet.YesNoBetVote;
import com.tvj.byf.service.BetService;
import com.tvj.byf.converter.DiscordObjectManager;
import com.tvj.byf.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.handle.impl.events.guild.channel.message.reaction.ReactionAddEvent;
import sx.blah.discord.handle.impl.obj.ReactionEmoji;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.handle.obj.IReaction;
import sx.blah.discord.handle.obj.IUser;
import sx.blah.discord.util.MessageHistory;

import java.util.List;

@Service
@Transactional
class BotCommandHandler {

    private final DiscordObjectManager discordObjectManager;
    private final BotReactionHandler reactionHandler;
    private final BetService betService;

    @Autowired
    public BotCommandHandler(DiscordObjectManager discordObjectManager, BotReactionHandler reactionHandler, BetService betController) {
        this.discordObjectManager = discordObjectManager;
        this.reactionHandler = reactionHandler;
        this.betService = betController;
    }

    void help(MessageReceivedEvent event) {
    }

    // todo maybe check for the length to see if a title is present
    void createYesNoBet(MessageReceivedEvent event) {
        // Create the bet
        String betTitle = event.getMessage().getContent().substring(5);
        User author = discordObjectManager.discorduserToUser(event.getAuthor());
        YesNoBet bet = betService.startYesNoBet(author, betTitle);
        // Send the message
        IMessage betMessage = BotUtils.sendMessageSynchrone(event.getChannel(), "Yes/No bet '" + bet.getTitle() + "' created by: " + bet.getCreater().getUsername() + " with id '" + bet.getId() + "' Vote by using the emotes!");
        // Add reactions to the message
        reactionHandler.addYesNoReactions(betMessage);
        // Save messageID
        bet.setOriginalMessageId(betMessage.getLongID());
        betService.updateBet(bet);
    }

    private void closeDeadlineBet(MessageReceivedEvent event, Bet bet) {
//        MessageHistory history = event.getChannel().getMessageHistoryFrom(bet.getCreateMessageId(), 1);
//        IMessage createMessage = history.getEarliestMessage();
//        if (bet.getClass() == YesNoBet.class) {
//            if (createMessage != null) {
//                closeDeadlineYesNoBet(createMessage, bet);
//            } else {
//                BotUtils.sendMessageAsynchrone(event.getChannel(), "Bot has gone offline while betting, please redo the bet");
//            }
//        }
    }

    private void closeDeadlineYesNoBet(IMessage createMessage, Bet bet) {
//        YesNoBet yesNoBet = betService.getYesNobet(bet.getId());
//        List<IReaction> recactions = createMessage.getReactions();
//        IReaction yesReaction = createMessage.getReactionByEmoji(ReactionEmoji.of("✅"));
//        IReaction noReaction = createMessage.getReactionByEmoji(ReactionEmoji.of("\uD83D\uDEAB"));
//        for (IUser user : yesReaction.getUsers()) {
//            betService.addVoteYesNoBet(yesNoBet, discordObjectManager.discorduserToUser(user), true);
//        }
//        for (IUser user : noReaction.getUsers()) {
//            betService.addVoteYesNoBet(yesNoBet, discordObjectManager.discorduserToUser(user), false);
//        }
    }

    void configureBet(MessageReceivedEvent event) {
        String message = event.getMessage().getContent().substring(5);
        long betId = Long.valueOf(message.substring(0, message.indexOf(" ")));
        Bet bet = betService.getBet(betId);

        if (bet == null) {
            BotUtils.sendMessageAsynchrone(event.getChannel(), "Could not find a bet with the id'" + betId + "'");
            return;
        }

        String command = message.substring(message.indexOf(" ") + 1);
        if (command.contains(" ")) {
            command = command.substring(0, command.indexOf(" "));
        }

        switch (command) {
            case "Close":
                closeDeadlineBet(event, bet);
                break;
            default:
                BotUtils.sendMessageAsynchrone(event.getChannel(), "Did not recognize command '" + command + "'");
        }
    }

    void reactionAdded(ReactionAddEvent event) {
        String message = event.getMessage().getContent();
        String bettype = message.substring(0, message.indexOf(" "));

        switch (bettype) {
            case "Yes/No":
                handleYesNoReactions(event);
                break;
            default:
                BotUtils.sendMessageAsynchrone(event.getChannel(), "wups, something went wrong nigga");
        }
    }

    private void handleYesNoReactions(ReactionAddEvent event) {
        ReactionEmoji reaction =  event.getReaction().getEmoji();

        if(reaction.equals(ReactionEmoji.of("✅")) || reaction.equals(ReactionEmoji.of("\uD83D\uDEAB"))){
            User voter = discordObjectManager.discorduserToUser(event.getUser());
            Boolean answer = reaction.equals(ReactionEmoji.of("✅"));
           betService.addYesNoVote(event.getMessage().getLongID(), voter, answer);
        }
    }
}
