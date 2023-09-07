package com.redisdemo.entities;

import lombok.Data;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

import javax.persistence.Id;

@RedisHash("Role")
@Data
public class RedisRole {

    @Id
    private Long id;

    @Indexed
    private String name;

    public RedisRole(Long id, String name) {
        this.id = id;
        this.name = name;
    }
}
