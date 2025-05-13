package com.example.WebLearn.controller;

import com.example.WebLearn.model.dto.UserChangePassword;
import com.example.WebLearn.model.request.LoginRequest;
import com.example.WebLearn.model.request.RegisterRequest;
import com.example.WebLearn.model.response.Response;
import com.example.WebLearn.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class UserController {
    @Autowired
    private UserService userService;
    @PostMapping("/login")
    public ResponseEntity<Response<Object>> login(@RequestBody LoginRequest loginRequest) {
        return userService.login(loginRequest.getEmail(), loginRequest.getPassword(), loginRequest.getRole());
    }
    @PostMapping("/register")
    public ResponseEntity<Response<Object>> register(@RequestBody RegisterRequest registerRequest) {
        return userService.register(registerRequest);
    }
    @GetMapping("/myinfo")
    public ResponseEntity<Response<Object>> getMyInfo() {
        return userService.getMyInfo();
    }

    @PutMapping(value = "/change-password")
    public ResponseEntity<Response<Object>> changePassword(@RequestBody UserChangePassword userChangePassword) {
        return this.userService.changePassword(userChangePassword);
    }
}
