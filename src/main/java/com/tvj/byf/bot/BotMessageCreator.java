package com.tvj.byf.bot;

import com.tvj.byf.domain.Bet;
import com.tvj.byf.domain.YesNoBet.YesNoBet;
import org.springframework.stereotype.Component;
import sx.blah.discord.api.internal.json.objects.EmbedObject;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.util.EmbedBuilder;

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
}
