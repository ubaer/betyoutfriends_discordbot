package com.tvj.byf.dao;

import com.tvj.byf.domain.Bet;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by Uber on 30-12-2017. betyoutfriends_discordbot
 */
@Repository
public interface BetJPA extends CrudRepository<Bet, Long> {
    Bet getByOriginalMessageId(long originalMessageId);
}
