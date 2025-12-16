package org.dpnam28.foodcouriers.presentation.mapper;

import org.dpnam28.foodcouriers.domain.entity.Restaurant;
import org.dpnam28.foodcouriers.presentation.dto.restaurant.RestaurantSearchResponse;
import org.dpnam28.foodcouriers.presentation.dto.user.UserUpdateRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface RestaurantMapper {
    Restaurant toRestaurant(UserUpdateRequest request);

    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "user.fullName")
    @Mapping(target = "address", source = "user.address")
    @Mapping(target = "location", source = "user.location.city")
    RestaurantSearchResponse toRestaurantSearchResponse(Restaurant restaurant);
}
