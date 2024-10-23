package com.example.urlshortner.controller;

import com.example.urlshortner.dto.UserRequestDTO;
import com.example.urlshortner.exception.UserAlreadyRegisteredException;
import com.example.urlshortner.model.User;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import com.example.urlshortner.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;



@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/add")
    public ResponseEntity<String> addUser(@RequestBody @Valid UserRequestDTO userRequestDTO) throws UserAlreadyRegisteredException {
        User user = userService.addUser(userRequestDTO);

        if(user == null){
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok("User created with username: " + user.getEmail());
    }
}