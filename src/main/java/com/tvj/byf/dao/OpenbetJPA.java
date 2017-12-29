package com.tvj.byf.dao;

import com.tvj.byf.domain.OpenBet;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface OpenbetJPA extends CrudRepository<OpenBet, Long> {
}
