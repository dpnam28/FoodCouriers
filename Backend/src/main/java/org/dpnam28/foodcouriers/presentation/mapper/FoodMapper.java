package org.dpnam28.foodcouriers.presentation.mapper;

import org.dpnam28.foodcouriers.domain.entity.Food;
import org.dpnam28.foodcouriers.presentation.dto.food.FoodResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface FoodMapper {
    @Mapping(target = "categoryId", source = "category.id")
    @Mapping(target = "categoryName", source = "category.name")
    @Mapping(target = "restaurantId", source = "restaurant.id")
    @Mapping(target = "isActive", expression = "java(food.isActive())")
    FoodResponse toFoodResponse(Food food);

    List<FoodResponse> toFoodResponses(List<Food> foods);
}
