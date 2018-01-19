package com.tvj.byf.service;

import com.tvj.byf.dao.BetJPA;
import com.tvj.byf.dao.OpenbetJPA;
import com.tvj.byf.domain.*;
import com.tvj.byf.domain.YesNoBet.YesNoBet;
import com.tvj.byf.domain.YesNoBet.YesNoBetHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
public class BetService {
    private OpenbetJPA openbetJPA;
    private BetJPA betJPA;
    private YesNoBetHandler yesNoBetHandler;

    @Autowired
    public BetService(OpenbetJPA openbetJPA, BetJPA betJPA, YesNoBetHandler yesNoBetHandler) {
        this.openbetJPA = openbetJPA;
        this.betJPA = betJPA;
        this.yesNoBetHandler = yesNoBetHandler;
    }

    public OpenBet startOpenBet(User creater, String title) {
        OpenBet bet = new OpenBet(creater, title);
        bet = openbetJPA.save(bet);
        return bet;
    }

    public Bet getBet(long id) {
        return betJPA.findOne(id);
    }

    public void addYesNoVote(Long originalMessageId, User voter, Boolean vote) {
        yesNoBetHandler.addVote(originalMessageId, voter, vote);
    }

    public YesNoBet startYesNoBet(User author, String betTitle) {
        return yesNoBetHandler.startYesNoBet(author, betTitle);
    }

    public void removeYesNoVote(Long originalMessageId, User voter){
        yesNoBetHandler.removeVote(originalMessageId, voter);
    }

    public void updateBet(Bet bet) {
        betJPA.save(bet);
    }
}
