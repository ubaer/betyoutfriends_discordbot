package com.tvj.byf.domain.YesNoBet;

import com.tvj.byf.domain.Bet;
import com.tvj.byf.domain.BetStatus;
import com.tvj.byf.domain.BetType;
import com.tvj.byf.domain.User;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import java.util.*;

/**
 * Created by Uber on 29-12-2017. betyoutfriends_discordbot
 */

@Entity
public class YesNoBet extends Bet {
    @ManyToMany(cascade = {CascadeType.ALL})
    private List<YesNoBetVote> voteList;

    YesNoBet(User creater, String title) {
        super(creater, title);
        voteList = new ArrayList<>();
        this.type = BetType.YesNo;
    }

    public YesNoBet() {
        super();
    }

    private boolean answer;

    public List<YesNoBetVote> getVoteList() {
        return voteList;
    }

    public void setAnswer(boolean answer) {
        this.answer = answer;
    }

    void addVote(User user, Boolean answer) {
        if (this.getStatus() == BetStatus.Open) {
            Optional<YesNoBetVote> optionalVote = voteList.stream().filter(v -> Objects.equals(v.getVoter().getId(), user.getId())).findFirst();
            YesNoBetVote vote;

            if (optionalVote.isPresent()) {
                vote = optionalVote.get();
                vote.setVote(answer);
            } else {
                vote = new YesNoBetVote(this.getId(),user, answer);

                voteList.add(vote);
            }
        }
    }

    void removeVote(User user) {
        if (this.getStatus() == BetStatus.Open) {
            Optional<YesNoBetVote> optionalVote = voteList.stream().filter(v -> Objects.equals(v.getVoter().getId(), user.getId())).findFirst();
            optionalVote.ifPresent(yesNoBetVote -> voteList.remove(yesNoBetVote));
        }
    }

    public boolean isAnswer() {
        return answer;
    }
}
