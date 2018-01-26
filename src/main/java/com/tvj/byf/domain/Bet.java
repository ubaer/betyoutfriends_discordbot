package com.tvj.byf.domain;

import javax.lang.model.util.SimpleElementVisitor6;
import javax.persistence.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
public abstract class Bet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @ManyToOne(fetch = FetchType.EAGER)
    private User creater;
    @ManyToMany()
    private List<User> referees;
    private Date createDate;
    private String title;
    private String description;
    private BetStatus status;
    private Date betDeadline;
    @ManyToMany()
    private List<User> winners;
    @ManyToMany()
    private List<User> losers;
    private long originalMessageId;
    protected BetType type;

    private String stakes;

    protected Bet(User creater, String title) {
        winners = new ArrayList<>();
        losers = new ArrayList<>();
        referees = new ArrayList<>();
        createDate = new Date();
        this.creater = creater;
        this.title = title;
        this.description = "";
        this.status = BetStatus.Open;
        this.stakes = "";
    }

    protected Bet() {
    }

    public User getCreater() {
        return creater;
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

    public void setDeadline(Date betDeadline) {
        this.betDeadline = betDeadline;
    }

    public List<User> getWinners() {
        return winners;
    }

    public void setWinners(List<User> winners) {
        this.winners = winners;
    }

    public List<User> getLosers() {
        return losers;
    }

    public void setLosers(List<User> losers) {
        this.losers = losers;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setStatus(BetStatus status) {
        if (this.getStatus() != BetStatus.Finished) {
            this.status = status;
        }
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

    public String getStakes() {
        return stakes;
    }

    public void setStakes(String stakes) {
        if (this.getStatus() != BetStatus.Finished) {
            this.stakes = stakes;
        }
    }

    public long getId() {
        return id;
    }

    public void setOriginalMessageId(long originalMessageId) {
        this.originalMessageId = originalMessageId;
    }

    public long getOriginalMessageId() {
        return originalMessageId;
    }

    public BetType getType() {
        return type;
    }

    public void setType(BetType type) {
        this.type = type;
    }

    public abstract boolean finishBet();

    public abstract void setAnswer(String answer);

    abstract public String getAnswer();
}
