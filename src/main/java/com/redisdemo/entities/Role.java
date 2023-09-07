package com.redisdemo.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.redisdemo.enums.RoleType;
import lombok.*;

import javax.persistence.*;
import java.util.Set;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "roles")
public class Role extends BaseEntity {

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private RoleType name;


    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "roles")
    @JsonIgnore
    private Set<User> users;

    public Role(RoleType name) {
        this.name = name;
    }

}
