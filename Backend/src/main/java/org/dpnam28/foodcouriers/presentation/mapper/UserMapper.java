package org.dpnam28.foodcouriers.presentation.mapper;

import org.dpnam28.foodcouriers.domain.entity.User;
import org.dpnam28.foodcouriers.presentation.dto.user.UserCreationRequest;
import org.dpnam28.foodcouriers.presentation.dto.user.UserUpdateRequest;
import org.dpnam28.foodcouriers.presentation.dto.user.UserResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "email", ignore = true)
    @Mapping(target = "role", ignore = true)
    @Mapping(target = "location", ignore = true)
    @Mapping(target = "courier", ignore = true)
    @Mapping(target = "restaurant", ignore = true)
    @Mapping(target = "customer", ignore = true)
    @Mapping(target = "cartItems", ignore = true)
    User toUser(UserUpdateRequest user);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "courier", ignore = true)
    @Mapping(target = "restaurant", ignore = true)
    @Mapping(target = "customer", ignore = true)
    @Mapping(target = "cartItems", ignore = true)
    @Mapping(target = "location", ignore = true)
    User toUser(UserCreationRequest user);

    @Mapping(target = "location", ignore = true)
    @Mapping(target = "description", source = "restaurant.description")
    @Mapping(target = "bannerImage", source = "restaurant.bannerImage")
    @Mapping(target = "deliveryFee", source = "restaurant.deliveryFee")
    @Mapping(target = "isAvailable", source = "courier.isAvailable")
    UserResponse toUserResponse(User user);
}
