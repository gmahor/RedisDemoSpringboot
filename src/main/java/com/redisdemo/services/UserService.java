package com.redisdemo.services;

import com.redisdemo.dtos.PageDTO;
import com.redisdemo.dtos.UserDTO;
import com.redisdemo.dtos.UserUpdateDTO;
import com.redisdemo.entities.RedisRole;
import com.redisdemo.entities.RedisUser;
import com.redisdemo.entities.Role;
import com.redisdemo.entities.User;
import com.redisdemo.enums.RoleType;
import com.redisdemo.repositories.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Slf4j
@Service
public class UserService {

    private final RoleRepository roleRepository;

    private final RedisRoleRepository redisRoleRepository;

    private final UserRepository userRepository;

    private final RedisUserRepository redisUserRepository;

    private final CustomSortingAndPagination customSortingAndPagination;

    @Autowired
    public UserService(RoleRepository roleRepository,
                       RedisRoleRepository redisRoleRepository,
                       UserRepository userRepository,
                       RedisUserRepository redisUserRepository,
                       CustomSortingAndPagination customSortingAndPagination) {
        this.roleRepository = roleRepository;
        this.redisRoleRepository = redisRoleRepository;
        this.userRepository = userRepository;
        this.redisUserRepository = redisUserRepository;
        this.customSortingAndPagination = customSortingAndPagination;
    }


    public RedisUser createUser(UserDTO userDTO) {
        User user = new User();
        user.setUsername(userDTO.getUsername());
        user.setEmail(userDTO.getEmail());
        user.setPassword(userDTO.getPassword());
        user.setEnabled(false);
        Set<Role> roles = new HashSet<>();
        Role role = roleRepository.findByName(RoleType.ROLE_USER);
        if (role != null) {
            roles.add(role);
        }
        user.setRoles(roles);
        User savedUser = userRepository.save(user);
        log.info("User saved in Database!!");
        return this.savedUserInRedis(savedUser);
    }

    public RedisUser savedUserInRedis(User user) {
        RedisUser redisUser = new RedisUser();
        redisUser.setId(user.getId());
        redisUser.setCreatedAt(user.getCreatedAt());
        redisUser.setUpdatedAt(user.getUpdatedAt());
        redisUser.setUsername(user.getUsername());
        redisUser.setEmail(user.getEmail());
        redisUser.setPassword(user.getPassword());
        redisUser.setEnabled(false);
        Set<RedisRole> redisRoles = new HashSet<>();
        RedisRole redisRole = redisRoleRepository.findByName(RoleType.ROLE_USER.name());
        if (redisRole != null) {
            redisRoles.add(redisRole);
        }
        redisUser.setRedisRoles(redisRoles);
        RedisUser savedUser = redisUserRepository.save(redisUser);
        log.info("User saved in redis!!");
        return savedUser;
    }


    public PageDTO getUsers(Integer page, Integer size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<RedisUser> allUsers = customSortingAndPagination.findAll(pageable);
        PageDTO pageDTO = new PageDTO();
        pageDTO.setContent(allUsers.getContent());
        pageDTO.setSize(allUsers.getSize());
        pageDTO.setTotalPages(allUsers.getTotalPages());
        pageDTO.setTotalRecords(allUsers.getTotalElements());
        return pageDTO;
    }

    public Object getUser(Long id) {
        Optional<RedisUser> optionalRedisUser = redisUserRepository.findById(id);
        Optional<User> optionalUser = userRepository.findById(id);
        if (optionalUser.isPresent() && optionalRedisUser.isPresent()) {
            return optionalRedisUser.get();
        } else {
            return "User not found";
        }
    }

    public Object updateUser(UserUpdateDTO userUpdateDTO) {
        Optional<RedisUser> optionalRedisUser = redisUserRepository.findById(userUpdateDTO.getId());
        Optional<User> optionalUser = userRepository.findById(userUpdateDTO.getId());
        if (optionalUser.isPresent() && optionalRedisUser.isPresent()) {
            this.updateUserFromDb(userUpdateDTO, optionalUser.get());
            return this.updateUserFromRedis(userUpdateDTO, optionalRedisUser.get(), optionalUser.get());
        }
        return "User not found";
    }

    void updateUserFromDb(UserUpdateDTO userUpdateDTO, User user) {
        user.setUsername(userUpdateDTO.getUsername() != null ? userUpdateDTO.getUsername() : user.getUsername());
        user.setEmail(userUpdateDTO.getEmail() != null ? userUpdateDTO.getEmail() : user.getEmail());
        user.setEnabled(userUpdateDTO.isEnabled() ? userUpdateDTO.isEnabled() : user.isEnabled());
        userRepository.save(user);
        log.info("User updated successfully from DB");
    }

    RedisUser updateUserFromRedis(UserUpdateDTO userUpdateDTO, RedisUser redisUser, User user) {
        redisUser.setUsername(userUpdateDTO.getUsername() != null ? userUpdateDTO.getUsername() : redisUser.getUsername());
        redisUser.setEmail(userUpdateDTO.getEmail() != null ? userUpdateDTO.getEmail() : redisUser.getEmail());
        redisUser.setEnabled(userUpdateDTO.isEnabled() ? userUpdateDTO.isEnabled() : redisUser.isEnabled());
        redisUser.setUpdatedAt(user.getUpdatedAt());
        RedisUser updatedRedisUser = redisUserRepository.save(redisUser);
        log.info("User updated successfully from redis");
        return updatedRedisUser;
    }

    public String deleteUser(Long id) {
        Optional<User> optionalUser = userRepository.findById(id);
        Optional<RedisUser> optionalRedisUser = redisUserRepository.findById(id);
        if (optionalUser.isPresent() && optionalRedisUser.isPresent()) {
            this.deleteUserFromDb(optionalUser.get());
            this.deleteUserFromRedis(optionalRedisUser.get());
            return "User deleted successfully";
        }
        return "User not found with this id";
    }

    void deleteUserFromDb(User user) {
        userRepository.delete(user);
        log.info("User is deleted from Database!");
    }

    void deleteUserFromRedis(RedisUser redisUser) {
        redisUserRepository.delete(redisUser);
        log.info("User is deleted from redis");
    }


}
