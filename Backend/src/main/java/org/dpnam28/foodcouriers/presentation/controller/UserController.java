package org.dpnam28.foodcouriers.presentation.controller;

import org.dpnam28.foodcouriers.domain.dto.ApiResponse;
import org.dpnam28.foodcouriers.domain.entity.Restaurant;
import org.dpnam28.foodcouriers.domain.entity.User;
import org.dpnam28.foodcouriers.presentation.dto.user.UserCreationRequest;
import org.dpnam28.foodcouriers.presentation.dto.user.UserResponse;
import org.dpnam28.foodcouriers.presentation.dto.user.UserUpdateRequest;
import org.dpnam28.foodcouriers.presentation.mapper.RestaurantMapper;
import org.dpnam28.foodcouriers.presentation.mapper.UserMapper;
import org.dpnam28.foodcouriers.usecase.UserUseCase;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final UserUseCase userUseCase;
    private final UserMapper userMapper;
    private final RestaurantMapper restaurantMapper;

    @PostMapping
    public ApiResponse<UserResponse> create(@RequestBody @Valid UserCreationRequest request) {
        User user = userMapper.toUser(request);
        User userCreation = userUseCase.createUser(user, request.getLocationId());
        UserResponse userResponse = userMapper.toUserResponse(userCreation);
        return ApiResponse.apiResponseSuccess("Create account succeeded", userResponse);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ApiResponse<UserResponse> update(@PathVariable Long id,
                                            @ModelAttribute @Valid UserUpdateRequest request,
                                            @RequestPart(value = "bannerImage", required = false) MultipartFile bannerImage) {
        User updateRequest = userMapper.toUser(request);
        Restaurant updateRestaurant = restaurantMapper.toRestaurant(request);
        User userUpdate = userUseCase.updateUser(id, updateRequest, updateRestaurant, bannerImage);
        UserResponse userResponse = userMapper.toUserResponse(userUpdate);
        return ApiResponse.apiResponseSuccess("Update account succeeded", userResponse);
    }

    @GetMapping("/{id}")
    public ApiResponse<UserResponse> findUserById(@PathVariable Long id) {
        User user = userUseCase.findById(id);
        UserResponse userResponse = userMapper.toUserResponse(user);
        if ("ROLE_RESTAURANT".equals(user.getRole()) && user.getRestaurant() != null) {
            userResponse.setDescription(user.getRestaurant().getDescription());
            userResponse.setBannerImage(user.getRestaurant().getBannerImage());
        } else if ("ROLE_COURIER".equals(user.getRole()) && user.getCourier() != null) {
            userResponse.setIsAvailable(user.getCourier().getIsAvailable());
        }
        return ApiResponse.apiResponseSuccess("Get account succeeded", userResponse);
    }
}
