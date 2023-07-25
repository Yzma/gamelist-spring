package com.game.gamelist.controller;

import com.game.gamelist.entity.User;
import com.game.gamelist.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class HelloWorld {
    private final UserRepository userRepository;

    @GetMapping("/hello")
    public List<User> helloWorld() {
        return userRepository.findAll();
    }
}
