package com.tvj.byf.domain;

import com.tvj.byf.dao.OpenbetJPA;

import javax.persistence.Entity;

@Entity
public class OpenBet extends Bet {
    public OpenBet(User creater, String title) {
        super(creater, title);
    }

    public OpenBet(){}
}
