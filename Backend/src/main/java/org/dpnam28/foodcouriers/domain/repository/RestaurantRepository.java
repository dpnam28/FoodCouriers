package org.dpnam28.foodcouriers.domain.repository;

import org.dpnam28.foodcouriers.domain.entity.Restaurant;

public interface RestaurantRepository {
    void save(Restaurant restaurant);
    Restaurant findById(Long id);
}
