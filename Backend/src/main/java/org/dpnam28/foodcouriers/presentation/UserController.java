package org.dpnam28.foodcouriers.presentation;

import org.dpnam28.foodcouriers.presentation.dto.request.UserCreationRequest;
import org.dpnam28.foodcouriers.presentation.dto.request.UserUpdateRequest;
import org.dpnam28.foodcouriers.presentation.dto.response.ApiResponse;
import org.dpnam28.foodcouriers.presentation.dto.response.UserResponse;
import org.dpnam28.foodcouriers.usecase.UserUseCase;
import org.springframework.web.bind.annotation.*;
import tools.jackson.databind.json.JsonMapper;

@RestController
@RequestMapping("/users")
public class UserController {
    private final UserUseCase userUseCase;
    private final JsonMapper.Builder builder;

    public UserController(UserUseCase userUseCase, JsonMapper.Builder builder) {
        this.userUseCase = userUseCase;
        this.builder = builder;
    }

    @PostMapping
    public ApiResponse<UserResponse> save(@RequestBody UserCreationRequest user) {
        return null;
    }

    @PutMapping
    public ApiResponse<UserResponse> update(@RequestBody UserUpdateRequest user) {
        return null;
    }
}
