package org.dpnam28.foodcouriers.presentation.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dpnam28.foodcouriers.domain.entity.Location;
import org.dpnam28.foodcouriers.domain.entity.User;
import org.dpnam28.foodcouriers.presentation.dto.user.UserCreationRequest;
import org.dpnam28.foodcouriers.presentation.dto.user.UserUpdateRequest;
import org.dpnam28.foodcouriers.domain.dto.ApiResponse;
import org.dpnam28.foodcouriers.presentation.dto.user.UserResponse;
import org.dpnam28.foodcouriers.presentation.mapper.UserMapper;
import org.dpnam28.foodcouriers.usecase.UserUseCase;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final UserUseCase userUseCase;
    private final UserMapper userMapper;

    @PostMapping
    public ApiResponse<UserResponse> save(@RequestBody UserCreationRequest request) {
        User user = userMapper.toUser(request);
        User userCreation = userUseCase.createUser(user, request.getLocationId());
        UserResponse userResponse = userMapper.toUserResponse(userCreation);
        return ApiResponse.apiResponseSuccess("Create account succeeded", userResponse) ;
    }

    @PutMapping("/{id}")
    public ApiResponse<UserResponse> update(@PathVariable Long id, @RequestBody UserUpdateRequest request) {
        User user = userMapper.toUser(request);
        Long locationId = request.getLocationId();
        User userCreation = userUseCase.updateUser(id, user, locationId);
        UserResponse userResponse = userMapper.toUserResponse(userCreation);
        return ApiResponse.apiResponseSuccess("Update account succeeded", userResponse) ;
    }
}
