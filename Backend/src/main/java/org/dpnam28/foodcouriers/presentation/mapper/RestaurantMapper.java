package org.dpnam28.foodcouriers.presentation.mapper;

import org.dpnam28.foodcouriers.domain.entity.Restaurant;
import org.dpnam28.foodcouriers.presentation.dto.user.UserUpdateRequest;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RestaurantMapper {
    Restaurant toRestaurant(UserUpdateRequest request);
}
