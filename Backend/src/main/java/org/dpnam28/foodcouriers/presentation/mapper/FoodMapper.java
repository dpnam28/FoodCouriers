package org.dpnam28.foodcouriers.presentation.mapper;

import org.dpnam28.foodcouriers.domain.entity.Food;
import org.dpnam28.foodcouriers.presentation.dto.food.FoodCreateRequest;
import org.dpnam28.foodcouriers.presentation.dto.food.FoodResponse;
import org.dpnam28.foodcouriers.presentation.dto.food.FoodUpdateRequest;
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

    @Mapping(target = "category.id", source = "categoryId")
    @Mapping(target = "restaurant.id", source = "restaurantId")
    Food toFood(FoodCreateRequest request);

    @Mapping(target = "category.id", source = "categoryId")
    Food toFood(FoodUpdateRequest request);

    List<FoodResponse> toFoodResponses(List<Food> foods);
}
