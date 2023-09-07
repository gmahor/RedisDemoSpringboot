package com.redisdemo.dtos;

import lombok.Data;

@Data
public class UserUpdateDTO {

    private Long id;

    private String username;

    private String email;

    private boolean enabled;

}
