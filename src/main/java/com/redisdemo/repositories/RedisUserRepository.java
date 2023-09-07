package com.redisdemo.repositories;

import com.redisdemo.entities.RedisUser;
import com.redisdemo.entities.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RedisUserRepository extends CrudRepository<RedisUser, Long> {
}
