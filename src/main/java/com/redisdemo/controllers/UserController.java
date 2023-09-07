package com.redisdemo.controllers;

import com.redisdemo.dtos.PageDTO;
import com.redisdemo.dtos.UserDTO;
import com.redisdemo.dtos.UserUpdateDTO;
import com.redisdemo.entities.RedisUser;
import com.redisdemo.services.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/createUser")
    public ResponseEntity<Object> createUser(@RequestBody UserDTO userDTO) {
        try {
            RedisUser user = userService.createUser(userDTO);
            if (user != null)
                return new ResponseEntity<>(user, HttpStatus.OK);
        } catch (Exception e) {
            log.info("Error while creating user - ", e);
        }
        return new ResponseEntity<>("Error while creating user!", HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/getUsers")
    public ResponseEntity<Object> getUsers(@RequestParam(defaultValue = "0") Integer page, @RequestParam(defaultValue = "2") Integer size) {
        try {
            PageDTO pageDTO = userService.getUsers(page, size);
            if (pageDTO.getTotalRecords() > 0)
                return new ResponseEntity<>(pageDTO, HttpStatus.OK);
            else
                return new ResponseEntity<>("Users not found", HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            log.info("Error while getting users - ", e);
        }
        return new ResponseEntity<>("Error while getting users!", HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/getUser/{id}")
    public ResponseEntity<Object> getUser(@PathVariable(name = "id") Long id) {
        try {
            Object userObj = userService.getUser(id);
            if (userObj.equals("User not found")) {
                return new ResponseEntity<>(userObj, HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>(userObj, HttpStatus.OK);
        } catch (Exception e) {
            log.info("Error while getting user - ", e);
        }
        return new ResponseEntity<>("Error while getting user!", HttpStatus.BAD_REQUEST);
    }


    @PostMapping("/updateUser")
    public ResponseEntity<Object> updateUser(@RequestBody UserUpdateDTO userUpdateDTO) {
        try {
            Object userObj = userService.updateUser(userUpdateDTO);
            if (userObj.equals("User not found")) {
                return new ResponseEntity<>(userObj, HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>(userObj, HttpStatus.OK);
        } catch (Exception e) {
            log.info("Error while updating user - ", e);
        }
        return new ResponseEntity<>("Error while updating user!", HttpStatus.BAD_REQUEST);
    }

    @DeleteMapping("/deleteUser/{id}")
    public ResponseEntity<Object> deleteUser(@PathVariable(name = "id") Long id) {
        try {
            String msg = userService.deleteUser(id);
            if (msg.equals("User deleted successfully")) {
                return new ResponseEntity<>(msg, HttpStatus.OK);
            }
            return new ResponseEntity<>(msg, HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            log.info("Error while deleting user - ", e);
        }
        return new ResponseEntity<>("Error while deleting user!", HttpStatus.BAD_REQUEST);
    }


}
