package com.tvj.byf.domain.YesNoBet;

import com.tvj.byf.domain.Bet;
import com.tvj.byf.domain.BetStatus;
import com.tvj.byf.domain.BetType;
import com.tvj.byf.domain.User;
import org.apache.commons.lang3.BooleanUtils;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.springframework.format.annotation.DateTimeFormat;
import sun.reflect.CallerSensitive;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToMany;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by Uber on 29-12-2017. betyoutfriends_discordbot
 */

@Entity
public class YesNoBet extends Bet {
    @ManyToMany(fetch = FetchType.EAGER)
    @Cascade({CascadeType.ALL})
    private List<YesNoBetVote> voteList;

    YesNoBet(User creater, String title) {
        super(creater, title);
        voteList = new ArrayList<>();
        this.type = BetType.YesNo;
        this.answer = -1;
    }

    public YesNoBet() {
        super();
    }


    private int answer;

    public List<YesNoBetVote> getVoteList() {
        return voteList;
    }

    public void setAnswer(boolean answer) {
        this.answer = answer ? 1 : 0;
    }

    public String getAnswer() {
        if (answer == 1) {
            return "Yes";
        } else {
            return "No";
        }
    }

    void addVote(User user, Boolean answer) {
        if (this.getStatus() != BetStatus.Open) {
            return;
        }
        if (this.getBetDeadline() != null) {
            if (this.getBetDeadline().before(new Date())) {
                return;
            }
        }
        Optional<YesNoBetVote> optionalVote = voteList.stream().filter(v -> Objects.equals(v.getVoter().getId(), user.getId())).findFirst();
        YesNoBetVote vote;

        if (optionalVote.isPresent()) {
            vote = optionalVote.get();
            vote.setVote(answer);
        } else {
            vote = new YesNoBetVote(this.getId(), user, answer);
            voteList.add(vote);
        }
    }

    void removeVote(User user) {
        if (this.getStatus() != BetStatus.Open) {
            return;
        }
        if (this.getBetDeadline() != null) {
            if (this.getBetDeadline().before(new Date())) {
                return;
            }
        }

        Optional<YesNoBetVote> optionalVote = voteList.stream().filter(v -> Objects.equals(v.getVoter().getId(), user.getId())).findFirst();
        optionalVote.ifPresent(yesNoBetVote -> voteList.remove(yesNoBetVote));

    }

    /**
     * @return list of users that had the correct answer
     */
    @Override
    public boolean finishBet() {
        if (answer < 0) {
            return false;
        }
        boolean boolanswer = BooleanUtils.toBoolean(answer);

        ArrayList<User> winners = new ArrayList<>();
        ArrayList<User> losers = new ArrayList<>();
        for (YesNoBetVote u : voteList) {
            if (u.getVote() == boolanswer) {
                winners.add(u.getVoter());
            } else {
                losers.add(u.getVoter());
            }
        }
        setWinners(winners);
        setLosers(losers);
        setStatus(BetStatus.Finished);

        return true;
    }

    @Override
    public void setAnswer(String answer) {
        answer = answer.replace(" ", "");
        if (answer.equals("True") || answer.equals("true") || answer.equals("Yes") || answer.equals("yes") || answer.equals("1")) {
            this.answer = 1;
        }
        if (answer.equals("False") || answer.equals("false") || answer.equals("No") || answer.equals("no") || answer.equals("0")) {
            this.answer = 0;
        }
    }
}
