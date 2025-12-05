package org.dpnam28.foodcouriers.presentation.controller;

import lombok.extern.slf4j.Slf4j;
import org.dpnam28.foodcouriers.domain.entity.User;
import org.dpnam28.foodcouriers.presentation.dto.request.UserCreationRequest;
import org.dpnam28.foodcouriers.presentation.dto.request.UserUpdateRequest;
import org.dpnam28.foodcouriers.domain.dto.ApiResponse;
import org.dpnam28.foodcouriers.presentation.dto.response.UserResponse;
import org.dpnam28.foodcouriers.presentation.mapper.UserMapper;
import org.dpnam28.foodcouriers.usecase.UserUseCase;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {
    private final UserUseCase userUseCase;
    private final UserMapper userMapper;

    public UserController(UserUseCase userUseCase, UserMapper userMapper) {
        this.userUseCase = userUseCase;
        this.userMapper = userMapper;
    }

    @PostMapping
    public ApiResponse<UserResponse> save(@RequestBody UserCreationRequest request) {
        User user = userMapper.toUser(request);
        User userCreation = userUseCase.createUser(user);
        UserResponse userResponse = userMapper.toUserResponse(userCreation);
        return apiResponse("Create account succeeded", userResponse) ;
    }

    @PutMapping("/{id}")
    public ApiResponse<UserResponse> update(@PathVariable Long id, @RequestBody UserUpdateRequest request) {
        User user = userMapper.toUser(request);
        User userCreation = userUseCase.updateUser(id, user);
        UserResponse userResponse = userMapper.toUserResponse(userCreation);
        return apiResponse("Update account succeeded", userResponse) ;
    }

    private static <T> ApiResponse<T> apiResponse(String message,T data) {
        return ApiResponse.<T>builder()
                .code(200)
                .message(message)
                .data(data)
                .build();
    }
}
