package org.dpnam28.foodcouriers.domain.repository;

import org.dpnam28.foodcouriers.domain.entity.Food;

import java.util.List;

public interface FoodRepository {
    Food save(Food food);

    Food findById(Long id);

    List<Food> findByCategoryId(Long categoryId);

    List<Food> findByRestaurantId(Long restaurantId);

    List<Food> searchByName(String keyword);
}
