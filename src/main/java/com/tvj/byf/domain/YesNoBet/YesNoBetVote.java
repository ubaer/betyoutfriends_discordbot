package com.tvj.byf.domain.YesNoBet;

import com.tvj.byf.domain.User;

import javax.persistence.*;

/**
 * Created by Uber on 16-1-2018. betyoutfriends_discordbot
 */
@Entity
public class YesNoBetVote {
    @Id
    long betId;
    @ManyToOne
    private User voter;
    private Boolean vote;


    User getVoter() {
        return voter;
    }

    public void setVoter(User voter) {
        this.voter = voter;
    }

    public Boolean getVote() {
        return vote;
    }

    public void setVote(Boolean vote) {
        this.vote = vote;
    }


    YesNoBetVote(long betId, User user, Boolean answer) {
        this.voter = user;
        this.vote = answer;
        this.betId = betId;
    }

    public YesNoBetVote() {
    }
}

