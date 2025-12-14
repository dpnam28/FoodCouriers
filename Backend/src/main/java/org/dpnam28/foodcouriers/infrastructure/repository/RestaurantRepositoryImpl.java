package org.dpnam28.foodcouriers.infrastructure.repository;

import lombok.RequiredArgsConstructor;
import org.dpnam28.foodcouriers.domain.entity.Restaurant;
import org.dpnam28.foodcouriers.domain.exception.AppException;
import org.dpnam28.foodcouriers.domain.exception.ErrorCode;
import org.dpnam28.foodcouriers.domain.repository.RestaurantRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

interface JpaRestaurantRepository extends JpaRepository<Restaurant, Long> {
}

@Repository
@RequiredArgsConstructor
public class RestaurantRepositoryImpl implements RestaurantRepository {
    private final JpaRestaurantRepository jpaRestaurantRepository;

    public void save(Restaurant restaurant) {
        jpaRestaurantRepository.save(restaurant);
    }

    @Override
    public Restaurant findById(Long id) {
        return jpaRestaurantRepository.findById(id)
                .orElse(null);
    }
}
