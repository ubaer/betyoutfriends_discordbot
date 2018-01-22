package com.tvj.byf.domain.OpenBet;

import com.tvj.byf.dao.OpenbetJPA;
import com.tvj.byf.domain.Bet;
import com.tvj.byf.domain.User;

import javax.persistence.Entity;

@Entity
public class OpenBet extends Bet {
    public OpenBet(User creater, String title) {
        super(creater, title);
    }

    public OpenBet(){}
}
