package com.tvj.byf.domain.YesNoBet;

import com.tvj.byf.dao.YesNobetJPA;
import com.tvj.byf.domain.BetStatus;
import com.tvj.byf.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sx.blah.discord.handle.impl.events.guild.channel.message.reaction.ReactionAddEvent;

import javax.transaction.Transactional;
import java.util.Optional;

/**
 * Created by Uber on 16-1-2018. betyoutfriends_discordbot
 */
@Service
@Transactional
public class YesNoBetHandler {
    private YesNobetJPA yesNobetJPA;

    @Autowired
    public YesNoBetHandler(YesNobetJPA yesNobetJPA) {
        this.yesNobetJPA = yesNobetJPA;
    }

    public YesNoBet getYesNobet(long id) {
        return yesNobetJPA.findOne(id);
    }

    public YesNoBet startYesNoBet(User creater, String title) {
        YesNoBet bet = new YesNoBet(creater, title);
        bet = yesNobetJPA.save(bet);
        return bet;
    }

    public void addVote(long originalBetMessageId, User user, Boolean answer) {
        YesNoBet bet = yesNobetJPA.findByOriginalMessageId(originalBetMessageId);
        bet.addVote(user, answer);
        yesNobetJPA.save(bet);
    }

    public void removeVote(Long originalBetMessageId, User voter) {
        YesNoBet bet = yesNobetJPA.findByOriginalMessageId(originalBetMessageId);
        bet.removeVote(voter);
        yesNobetJPA.save(bet);
    }
}
