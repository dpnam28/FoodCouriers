package org.dpnam28.foodcouriers.usecase;

import lombok.RequiredArgsConstructor;
import org.dpnam28.foodcouriers.domain.entity.Food;
import org.dpnam28.foodcouriers.domain.entity.Restaurant;
import org.dpnam28.foodcouriers.domain.exception.AppException;
import org.dpnam28.foodcouriers.domain.exception.ErrorCode;
import org.dpnam28.foodcouriers.domain.repository.FoodRepository;
import org.dpnam28.foodcouriers.domain.repository.RestaurantRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RestaurantUseCase {

    private final RestaurantRepository restaurantRepository;
    private final FoodRepository foodRepository;

    public List<Restaurant> searchRestaurants(String name, String location) {
        return restaurantRepository.searchByNameAndLocation(name, location);
    }

    public List<Food> getFoodsByRestaurant(Long restaurantId) {
        Restaurant restaurant = restaurantRepository.findById(restaurantId);
        if (restaurant == null) {
            throw new AppException(ErrorCode.RESTAURANT_NOT_FOUND);
        }
        return foodRepository.findByRestaurantId(restaurantId);
    }

    public Restaurant getRestaurantById(Long id) {
        Restaurant restaurant = restaurantRepository.findById(id);
        if (restaurant == null) {
            throw new AppException(ErrorCode.RESTAURANT_NOT_FOUND);
        }
        return restaurant;
    }
}
