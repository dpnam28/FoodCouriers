package org.dpnam28.foodcouriers.presentation.controller;

import lombok.RequiredArgsConstructor;
import org.dpnam28.foodcouriers.domain.dto.ApiResponse;
import org.dpnam28.foodcouriers.domain.entity.Food;
import org.dpnam28.foodcouriers.domain.entity.Restaurant;
import org.dpnam28.foodcouriers.presentation.dto.food.FoodResponse;
import org.dpnam28.foodcouriers.presentation.dto.restaurant.RestaurantSearchResponse;
import org.dpnam28.foodcouriers.presentation.dto.restaurant.RestaurantWithFoodsResponse;
import org.dpnam28.foodcouriers.presentation.mapper.FoodMapper;
import org.dpnam28.foodcouriers.presentation.mapper.RestaurantMapper;
import org.dpnam28.foodcouriers.usecase.RestaurantUseCase;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/restaurants")
@RequiredArgsConstructor
public class RestaurantController {

    private final RestaurantUseCase restaurantUseCase;
    private final RestaurantMapper restaurantMapper;
    private final FoodMapper foodMapper;

    @GetMapping("/{id}")
    public ApiResponse<RestaurantWithFoodsResponse> getRestaurantWithFoods(@PathVariable Long id) {
        Restaurant restaurant = restaurantUseCase.getRestaurantById(id);
        List<Food> foods = restaurantUseCase.getFoodsByRestaurant(id);
        RestaurantWithFoodsResponse response = new RestaurantWithFoodsResponse();
        response.setRestaurant(restaurantMapper.toRestaurantResponse(restaurant));
        response.setFoods(foodMapper.toFoodResponses(foods));
        return ApiResponse.apiResponseSuccess("Get restaurant succeeded", response);
    }
    @GetMapping("/search")
    public ApiResponse<List<RestaurantSearchResponse>> searchRestaurants(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String location
    ) {
        List<Restaurant> restaurants = restaurantUseCase.searchRestaurants(name, location);
        List<RestaurantSearchResponse> responseList = restaurants.stream()
                .map(restaurantMapper::toRestaurantSearchResponse)
                .collect(Collectors.toList());
        return ApiResponse.apiResponseSuccess("Search restaurants succeeded", responseList);
    }

    @GetMapping("/{id}/foods")
    public ApiResponse<List<FoodResponse>> getFoodsByRestaurant(@PathVariable Long id) {
        List<Food> foods = restaurantUseCase.getFoodsByRestaurant(id);
        return ApiResponse.apiResponseSuccess("Get foods by restaurant succeeded", foodMapper.toFoodResponses(foods));
    }
}
