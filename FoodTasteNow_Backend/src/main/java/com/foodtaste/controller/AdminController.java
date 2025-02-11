package com.foodtaste.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.foodtaste.constant.AppConstants;
import com.foodtaste.dto.UserDTO;
import com.foodtaste.service.UserService;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping(AppConstants.APP_NAME + AppConstants.PRIVATE_ROUTE_TYPE + "/admin")
@Slf4j
public class AdminController {

    @Autowired
    private UserService userService;

    @GetMapping(AppConstants.GET_ALL)
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        log.info("Received request to fetch all users");

        List<UserDTO> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    @GetMapping(AppConstants.GET_BY_ID + "/{id}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable String id) {
        log.info("Received request to fetch user with ID: {}", id);

        UserDTO user = userService.getUserById(id);
        return ResponseEntity.ok(user);
    }

    @DeleteMapping(AppConstants.DELETE + "/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable String id) {
        log.info("Received request to delete user with ID: {}", id);

        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

}
