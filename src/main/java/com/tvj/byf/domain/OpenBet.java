package com.tvj.byf.domain;

import javax.persistence.Entity;

@Entity
public class OpenBet extends Bet {
    public OpenBet(User creater, String title, String a) {
        super(creater, title);
    }
}
