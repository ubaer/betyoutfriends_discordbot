package com.tvj.byf.dao;

import com.tvj.byf.domain.YesNoBet.YesNoBet;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by Uber on 30-12-2017. betyoutfriends_discordbot
 */
@Repository
@Transactional
public interface YesNobetJPA extends CrudRepository<YesNoBet, Long> {
    YesNoBet findByOriginalMessageId(long originalMessageId);
}
