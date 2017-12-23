package com.tvj.byf.domain;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public abstract class Bet {
    private long id;
    private User creater;
    private List<User> participants;
    private List<User> referees;
    private Date createDate;
    private String title;
    private String description;
    private BetStatus status;
    private Date betDeadline;
    private List<User> winners;

    Bet(User creater, String title) {
        //todo iets van een ID systeem enzo.
        id = 1;
        participants = new ArrayList<>();
        referees = new ArrayList<>();
        createDate = new Date();
        this.creater = creater;
        this.title = title;
        this.description = "";
        this.status = status;
    }

    public User getCreater() {
        return creater;
    }

    public List<User> getParticipants() {
        return participants;
    }

    public List<User> getReferees() {
        return referees;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public BetStatus getStatus() {
        return status;
    }

    public Date getBetDeadline() {
        return betDeadline;
    }

    public void setBetDeadline(Date betDeadline) {
        this.betDeadline = betDeadline;
    }

    public List<User> getWinners() {
        return winners;
    }

    public void setWinners(List<User> winners) {
        this.winners = winners;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setStatus(BetStatus status) {
        this.status = status;
    }

    public List<User> addReferee(User referee) {
        if (!referees.contains(referee)) {
            referees.add(referee);
        }
        return referees;
    }

    public List<User> removeReferee(User referee) {
        if (referees.contains(referee)) {
            referees.remove(referee);
        }
        return referees;
    }

    public List<User> addParticipant(User participant) {
        if (!participants.contains(participant)) {
            participants.add(participant);
        }
        return participants;
    }

    public List<User> removeParticipant(User participant) {
        if (participants.contains(participant)) {
            participants.remove(participant);
        }
        return participants;
    }

    public long getId() {
        return id;
    }
}
