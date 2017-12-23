package com.tvj.byf.controller;

import com.tvj.byf.domain.*;

public class BetController {
    public static OpenBet startOpenBet(User creater, String title){
        OpenBet bet = new OpenBet(creater, title);
        bet.setDescription("Test description for bet");

        return bet;
    }
}
