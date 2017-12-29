package com.tvj.byf.service;

import com.tvj.byf.dao.OpenbetJPA;
import com.tvj.byf.domain.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;


@Service
@Transactional
public class BetService {
    private OpenbetJPA openbetJPA;

    @Autowired
    public BetService(OpenbetJPA openbetJPA) {
        this.openbetJPA = openbetJPA;
    }

    public OpenBet startOpenBet(User creater, String title) {
        OpenBet bet = new OpenBet(creater, title, "wat doe je nou database?");
        bet = openbetJPA.save(bet);
        return bet;
    }
}
