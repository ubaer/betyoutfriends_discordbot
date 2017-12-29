package com.tvj.byf.dao;

import com.tvj.byf.domain.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface UserJPA extends CrudRepository<User, String>{
}
