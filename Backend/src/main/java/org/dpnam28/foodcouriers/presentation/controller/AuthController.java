package org.dpnam28.foodcouriers.presentation.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.dpnam28.foodcouriers.domain.dto.ApiResponse;
import org.dpnam28.foodcouriers.domain.entity.User;
import org.dpnam28.foodcouriers.presentation.dto.auth.AuthLoginRequest;
import org.dpnam28.foodcouriers.presentation.dto.auth.AuthLoginResponse;
import org.dpnam28.foodcouriers.presentation.mapper.AuthMapper;
import org.dpnam28.foodcouriers.usecase.AuthUseCase;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthUseCase authUseCase;
    private final AuthMapper authMapper;

    @PostMapping("/login")
    public ApiResponse<AuthLoginResponse> login(@RequestBody @Valid AuthLoginRequest user){
        User userLogin = authUseCase.login(user.getEmail(), user.getPassword());
        AuthLoginResponse response = authMapper.toAuthLoginResponse(userLogin);
        return ApiResponse.apiResponseSuccess("Login succeeded", response);
    }
}
