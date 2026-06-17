package com.promptlabs.backend_for_frontend.controller;

import com.promptlabs.backend_for_frontend.dto.UserSummaryResponse;
import com.promptlabs.backend_for_frontend.service.UserService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class UsersController {
    private final UserService userService;

    @GetMapping
    public ResponseEntity<List<UserSummaryResponse>> listUsers() {
        return ResponseEntity.ok(
                userService.listUsers()
        );
    }
}
