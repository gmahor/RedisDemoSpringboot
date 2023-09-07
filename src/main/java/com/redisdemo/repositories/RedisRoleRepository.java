package com.redisdemo.repositories;

import com.redisdemo.entities.RedisRole;
import com.redisdemo.enums.RoleType;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RedisRoleRepository extends CrudRepository<RedisRole, Long> {
    RedisRole findByName(String name);

}
