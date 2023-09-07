package com.redisdemo.entities;


import lombok.Data;
import org.springframework.data.redis.core.RedisHash;

import javax.persistence.Id;
import java.time.Instant;
import java.util.Set;

@RedisHash("Users")
@Data
public class RedisUser {

    @Id
    private Long id;

    private Instant createdAt;

    private Instant updatedAt;

    private String username;

    private String password;

    private String email;

    private boolean enabled;

    private Set<RedisRole> redisRoles;
}
