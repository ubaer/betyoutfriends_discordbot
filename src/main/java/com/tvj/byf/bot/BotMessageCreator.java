package com.tvj.byf.bot;

import com.tvj.byf.domain.Bet;
import com.tvj.byf.domain.User;
import com.tvj.byf.domain.YesNoBet.YesNoBet;
import org.springframework.stereotype.Component;
import sx.blah.discord.api.internal.json.objects.EmbedObject;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.util.EmbedBuilder;

import java.util.List;

/**
 * Created by Uber on 21-1-2018. betyoutfriends_discordbot
 */
@Component
public class BotMessageCreator {

    public BotMessageCreator() {
    }

    EmbedObject createBet(Bet bet) {
        EmbedBuilder builder = new EmbedBuilder();

        builder.withAuthorName(bet.getCreater().getUsername());
        builder.withTitle(bet.getTitle());

        String description = "No description set yet";
        String deadline = "Deadline not set yet";
        String stakes = "No stakes set yet";

        if (bet.getBetDeadline() != null) {
            deadline = bet.getBetDeadline().toString();
        }
        if (!bet.getStakes().equals("")) {
            stakes = bet.getStakes();
        }
        if (!bet.getDescription().equals("")) {
            description = bet.getDescription();
        }

        builder.appendField("Description", description, false);
        builder.appendField("Bet type", bet.getType().toString(), true);
        builder.appendField("Deadline", deadline, true);
        builder.appendField("Bet id", String.valueOf(bet.getId()), true);
        builder.appendField("Stakes", stakes, true);
        builder.appendField("Status", bet.getStatus().toString(), true);
        builder.withFooterText("This is a Yes/No bet, you can vote using the ✅ and \uD83D\uDEAB reactions on this message. To reload after editing the bet use the \uD83D\uDD04 emoji");
        builder.withColor(255, 80, 80);

        return builder.build();
    }

    EmbedObject finishBet(Bet bet) {
        EmbedBuilder builder = new EmbedBuilder();

        String stakes = "There were no stakes set for this bet";
        if (!bet.getStakes().equals("")) {
            stakes = bet.getStakes();
        }
        builder.withAuthorName("Bet results");
        builder.withTitle(bet.getTitle());

        builder.appendField("The correct answer was:", bet.getAnswer(), false);
        builder.appendField("Winners", createWinnerString(bet.getWinners()), false);
        builder.appendField("Losers", createLosersString(bet.getLosers()), false);
        builder.appendField("Stakes", stakes, false);


        builder.withColor(102, 255, 102);
        return builder.build();
    }

    EmbedObject createHelpMessage() {
        EmbedBuilder builder = new EmbedBuilder();

        builder.withAuthorName("Bet your friends");
        builder.withTitle("Help!");

        builder.appendField("These type of bets are possible at this moment:", "Yes/No bets, use the '$Y/N?' for an example", false);

        builder.withColor(51, 153, 255);
        return builder.build();
    }

    EmbedObject createHelpYesNoMessage() {
        EmbedBuilder builder = new EmbedBuilder();

        builder.withAuthorName("Name of the bet creator");
        builder.withTitle("Create the bet with '$Y/N *Title of the bet*'");

        builder.appendField("Description", "Description: Your text here", false);
        builder.appendField("Bet type", "YesNo, can't be modified", true);
        builder.appendField("Deadline", "Deadline: dd/mm/yyyy hh:mm:ss", true);
        builder.appendField("Bet id", "Bet id, can't be modified", true);
        builder.appendField("Stakes", "Stake: Your text here", true);
        builder.appendField("Status", "Open, Close, Cancel, Finish", true);
        builder.withFooterText("This is an example of a Yes/No bet. All attributes can be set by typing '$*betId* + command'. Example: '$1 Description: New description for bet one'. Before using the finish command set the answer/outcome with: 'Answer: Yes(or No)' command");

        builder.withColor(51, 153, 255);
        return builder.build();
    }

    private String createWinnerString(List<User> winners) {
        StringBuilder stringBuilder = new StringBuilder();

        if (winners.size() > 0) {
            for (User u : winners) {
                stringBuilder.append("<@").append(u.getId()).append("> ");
            }
        } else {
            stringBuilder.append("There are no winners");
        }

        return stringBuilder.toString();
    }

    private String createLosersString(List<User> losersList) {
        StringBuilder stringBuilder = new StringBuilder();

        if (losersList.size() > 0) {
            for (User u : losersList) {
                stringBuilder.append("<@").append(u.getId()).append("> ");
            }
        } else {
            stringBuilder.append("There are no losers");
        }

        return stringBuilder.toString();
    }

}
