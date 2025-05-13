package com.example.WebLearn.service;

import com.example.WebLearn.model.dto.UserChangePassword;
import com.example.WebLearn.model.dto.UserDTO;
import com.example.WebLearn.model.request.RegisterRequest;
import com.example.WebLearn.model.response.Response;
import org.springframework.http.ResponseEntity;

public interface UserService {
    //Đăng nhập
    ResponseEntity<Response<Object>> login(String email, String password, String role);

    //Đăng kí
    ResponseEntity<Response<Object>> register(RegisterRequest registerRequest);

    ResponseEntity<Response<Object>> getMyInfo();

    ResponseEntity<Response<Object>> changePassword(UserChangePassword userChangePassword);
}
