package com.tvj.byf.bot;

import com.tvj.byf.domain.Bet;
import com.tvj.byf.domain.BetStatus;
import com.tvj.byf.domain.YesNoBet.YesNoBet;
import com.tvj.byf.service.BetService;
import com.tvj.byf.converter.DiscordObjectManager;
import com.tvj.byf.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sx.blah.discord.api.internal.json.objects.EmbedObject;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.handle.impl.events.guild.channel.message.reaction.ReactionAddEvent;
import sx.blah.discord.handle.impl.events.guild.channel.message.reaction.ReactionEvent;
import sx.blah.discord.handle.impl.events.guild.channel.message.reaction.ReactionRemoveEvent;
import sx.blah.discord.handle.impl.obj.ReactionEmoji;
import sx.blah.discord.handle.obj.IEmbed;
import sx.blah.discord.handle.obj.IMessage;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
class BotCommandHandler {

    private final DiscordObjectManager discordObjectManager;
    private final BotReactionHandler reactionHandler;
    private final BetService betService;
    private final BotMessageCreator messageCreator;
    private SimpleDateFormat df;


    @Autowired
    public BotCommandHandler(DiscordObjectManager discordObjectManager, BotReactionHandler reactionHandler, BetService betController, BotMessageCreator messageCreator) {
        this.discordObjectManager = discordObjectManager;
        this.reactionHandler = reactionHandler;
        this.betService = betController;
        this.messageCreator = messageCreator;
        df = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");
    }

    void help(MessageReceivedEvent event) {
    }

    // todo maybe check for the length to see if a title is present
    Boolean createYesNoBet(MessageReceivedEvent event) {
        // Create the bet
        String betTitle = event.getMessage().getContent().substring(5);
        User author = discordObjectManager.discorduserToUser(event.getAuthor());
        YesNoBet bet = betService.startYesNoBet(author, betTitle);

        // Send the message
        IMessage betMessage = BotUtils.sendMessageSynchrone(event.getChannel(), messageCreator.createBet(bet));

        // Add reactions to the message
        reactionHandler.addYesNoReactions(betMessage);
        reactionHandler.addReloadReaction(betMessage);
        // Save messageID
        bet.setOriginalMessageId(betMessage.getLongID());
        betService.updateBet(bet);
        return true;
    }


    Boolean configureBet(int betNumber, MessageReceivedEvent event) {
        String content = event.getMessage().getContent();
        String message = content.substring(content.indexOf(" "));
        Bet bet = betService.getBet(betNumber);

        if (bet == null) {
            BotUtils.sendMessageAsynchrone(event.getChannel(), "Could not find a bet with the id'" + betNumber + "'");
            return false;
        }

        String command = message.substring(message.indexOf(" ") + 1);
        if (command.contains(" ")) {
            // First set message to know which parameters the user has used before formatting the command
            message = command.substring(command.indexOf(" "));
            command = command.substring(0, command.indexOf(" "));
        }

        switch (command) {
            case "Close":
                bet.setStatus(BetStatus.Closed);
                break;
            case "Cancel":
                bet.setStatus(BetStatus.Canceled);
                break;
            case "Description:":
                bet.setDescription(message);
                break;
            case "Deadline:":
                try {
                    bet.setDeadline(df.parse(message.substring(1)));
                } catch (ParseException e) {
                    BotUtils.sendMessageAsynchrone(event.getChannel(), "Use this datetime format: dd/mm/yyyy hh:mm:ss");
                    return false;
                }
                break;
            case "Stake":
                bet.setStakes(message);
                break;
            default:
                BotUtils.sendMessageAsynchrone(event.getChannel(), "Did not recognize command '" + command + "'");
        }
        betService.updateBet(bet);
        return true;
    }

    void reactionAdded(ReactionAddEvent event) {
        List<IEmbed> embedObject = event.getMessage().getEmbeds();
        IEmbed message = embedObject.get(0);
        List<IEmbed.IEmbedField> fields = message.getEmbedFields();
        Optional<IEmbed.IEmbedField> embedBetType = fields.stream().filter(f -> f.getName().equals("Bet type")).findFirst();

        String bettype = embedBetType.get().getValue();

        switch (bettype) {
            case "YesNo":
                handleYesNoReactions(event);
                break;
            default:
                BotUtils.sendMessageAsynchrone(event.getChannel(), "wups, something went wrong nigga");
        }
    }

    void reactionRemoved(ReactionRemoveEvent event) {
        List<IEmbed> embedObject = event.getMessage().getEmbeds();
        IEmbed message = embedObject.get(0);
        List<IEmbed.IEmbedField> fields = message.getEmbedFields();
        Optional<IEmbed.IEmbedField> a = fields.stream().filter(f -> f.getName().equals("Bet type")).findFirst();

        String bettype = a.get().getValue();

        switch (bettype) {
            case "YesNo":
                handleYesNoReactionRemoved(event);
                break;
            default:
                BotUtils.sendMessageAsynchrone(event.getChannel(), "wups, something went wrong nigga");
        }
    }

    private void handleYesNoReactionRemoved(ReactionRemoveEvent event) {
        Long messageId = event.getMessageID();
        ReactionEmoji reaction = event.getReaction().getEmoji();
        if (reaction.equals(ReactionEmoji.of("✅"))) {
            User user = discordObjectManager.discorduserToUser(event.getUser());
            Boolean hasOtherVote = event.getMessage().getReactions().stream().filter(r -> r.getEmoji().equals(ReactionEmoji.of("\uD83D\uDEAB"))).findFirst().get().getUsers().stream().anyMatch(u -> u.getStringID().equals(user.getId()));

            if (hasOtherVote) {
                betService.addYesNoVote(messageId, user, false);
            } else {
                betService.removeYesNoVote(messageId, user);
            }
        }
        if (reaction.equals(ReactionEmoji.of("\uD83D\uDEAB"))) {
            User user = discordObjectManager.discorduserToUser(event.getUser());
            Boolean hasOtherVote = event.getMessage().getReactions().stream().filter(r -> r.getEmoji().equals(ReactionEmoji.of("✅"))).findFirst().get().getUsers().stream().anyMatch(u -> u.getStringID().equals(user.getId()));

            if (hasOtherVote) {
                betService.addYesNoVote(messageId, user, true);
            } else {
                betService.removeYesNoVote(messageId, user);
            }
        }
        if (reaction.equals(ReactionEmoji.of("\uD83D\uDD04"))) {
            reloadBetMessage(event);
        }
    }

    private void handleYesNoReactions(ReactionAddEvent event) {
        ReactionEmoji reaction = event.getReaction().getEmoji();

        if (reaction.equals(ReactionEmoji.of("✅")) || reaction.equals(ReactionEmoji.of("\uD83D\uDEAB"))) {
            User voter = discordObjectManager.discorduserToUser(event.getUser());
            Boolean answer = reaction.equals(ReactionEmoji.of("✅"));
            betService.addYesNoVote(event.getMessage().getLongID(), voter, answer);
        }
        if (reaction.equals(ReactionEmoji.of("\uD83D\uDD04"))) {
            reloadBetMessage(event);
        }
    }

    private void reloadBetMessage(ReactionEvent event) {
        IEmbed embed = event.getMessage().getEmbeds().get(0);
        IEmbed.IEmbedField embedField = embed.getEmbedFields().stream().filter(f -> f.getName().equals("Bet id")).findFirst().get();
        int bedId = Integer.parseInt(embedField.getValue());

        Bet foundBet = betService.getBet(bedId);
        EmbedObject embedObject = messageCreator.createBet(foundBet);
        event.getMessage().edit(embedObject);
    }
}
