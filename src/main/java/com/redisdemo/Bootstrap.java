package com.redisdemo;

import com.redisdemo.entities.RedisRole;
import com.redisdemo.entities.Role;
import com.redisdemo.enums.RoleType;
import com.redisdemo.repositories.RedisRoleRepository;
import com.redisdemo.repositories.RoleRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Component
public class Bootstrap implements ApplicationListener<ContextRefreshedEvent> {

    private final RoleRepository roleRepository;

    private final RedisRoleRepository redisRoleRepository;

    @Autowired
    public Bootstrap(RoleRepository roleRepository,
                     RedisRoleRepository redisRoleRepository) {
        this.roleRepository = roleRepository;
        this.redisRoleRepository = redisRoleRepository;
    }

    private void saveRolesInDB() {
        List<Role> roles = new ArrayList<>();
        Role userRole = new Role(RoleType.ROLE_USER);
        Role adminRole = new Role(RoleType.ROLE_ADMIN);
        roles.add(userRole);
        roles.add(adminRole);
        if (roleRepository.count() == 0 && redisRoleRepository.count() == 0) {
            List<Role> savedRoles = roleRepository.saveAll(roles);
            List<RedisRole> redisRoleList = savedRoles.stream().map(role -> new RedisRole(role.getId(), role.getName().name())).collect(Collectors.toList());
            redisRoleRepository.saveAll(redisRoleList);
            log.info("Roles save successfully");
        }
    }


    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        try {
            this.saveRolesInDB();
        } catch (Exception e) {
            log.error("Exception In On Application Event Service - ", e);
        }
    }
}
