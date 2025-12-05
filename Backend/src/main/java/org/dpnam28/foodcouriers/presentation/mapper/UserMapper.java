package org.dpnam28.foodcouriers.presentation.mapper;

import org.dpnam28.foodcouriers.domain.entity.User;
import org.dpnam28.foodcouriers.presentation.dto.user.UserCreationRequest;
import org.dpnam28.foodcouriers.presentation.dto.user.UserUpdateRequest;
import org.dpnam28.foodcouriers.presentation.dto.user.UserResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {
    @Mapping(target = "location", ignore = true)
    User toUser(UserUpdateRequest user);
    @Mapping(target = "location", ignore = true)
    User toUser(UserCreationRequest user);
    UserResponse toUserResponse(User user);
}
