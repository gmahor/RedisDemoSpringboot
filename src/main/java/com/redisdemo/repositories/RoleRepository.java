package com.redisdemo.repositories;

import com.redisdemo.entities.Role;
import com.redisdemo.enums.RoleType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Role findByName(RoleType name);
}
