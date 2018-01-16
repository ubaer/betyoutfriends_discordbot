package com.tvj.byf.bot;

import com.tvj.byf.domain.YesNoBet.YesNoBetVote;
import org.springframework.stereotype.Service;
import sx.blah.discord.handle.impl.events.guild.channel.message.reaction.ReactionAddEvent;
import sx.blah.discord.handle.impl.obj.ReactionEmoji;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.util.RateLimitException;
import sx.blah.discord.util.RequestBuffer;

import java.util.List;

/**
 * Created by Uber on 30-12-2017. betyoutfriends_discordbot
 */
@Service
class BotReactionHandler {
    void addYesNoReactions(IMessage message) {
        // Add Y and N emote to the message
        ReactionEmoji y = ReactionEmoji.of("✅");
        ReactionEmoji n = ReactionEmoji.of("\uD83D\uDEAB");

        RequestBuffer.request(() -> {
            try {
                message.addReaction(y);
            } catch (RateLimitException e) {
                System.out.println("Do some logging");
                throw e; // This makes sure that RequestBuffer will do the retry for you
            }
        });
        RequestBuffer.request(() -> {
            try {
                message.addReaction(n);
            } catch (RateLimitException e) {
                System.out.println("Do some logging");
                throw e; // This makes sure that RequestBuffer will do the retry for you
            }
        });
    }
}
