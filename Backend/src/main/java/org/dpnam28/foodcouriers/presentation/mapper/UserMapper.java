package org.dpnam28.foodcouriers.presentation.mapper;

import org.dpnam28.foodcouriers.domain.entity.User;
import org.dpnam28.foodcouriers.presentation.dto.request.UserCreationRequest;
import org.dpnam28.foodcouriers.presentation.dto.request.UserUpdateRequest;
import org.dpnam28.foodcouriers.presentation.dto.response.UserResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    User toUser(UserUpdateRequest user);
    User toUser(UserCreationRequest user);
    UserResponse toUserResponse(User user);
}
