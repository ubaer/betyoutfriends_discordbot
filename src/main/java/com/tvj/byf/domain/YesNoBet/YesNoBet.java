package com.tvj.byf.domain.YesNoBet;
import com.tvj.byf.domain.Bet;
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
    public YesNoBet(User creater, String title) {
        super(creater, title);
        voteList = new ArrayList<>();
    }

    public YesNoBet(){
        super();
    }

    private boolean answer;


    public void setAnswer(boolean answer) {
        this.answer = answer;
    }

    public void addVote(User user, Boolean answer) {
        Optional<YesNoBetVote> optionalVote = voteList.stream().filter(v -> Objects.equals(v.getVoter().getId(), user.getId())).findFirst();
        YesNoBetVote vote;

        if(optionalVote.isPresent()){
            vote = optionalVote.get();
            vote.setVote(answer);
        }
        else{
            vote = new YesNoBetVote(user, answer);

            voteList.add(vote);
        }
    }


    public boolean isAnswer() {
        return answer;
    }
}
