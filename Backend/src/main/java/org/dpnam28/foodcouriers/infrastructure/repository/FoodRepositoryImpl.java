package org.dpnam28.foodcouriers.infrastructure.repository;

import lombok.RequiredArgsConstructor;
import org.dpnam28.foodcouriers.domain.entity.Food;
import org.dpnam28.foodcouriers.domain.repository.FoodRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

interface JpaFoodRepository extends JpaRepository<Food, Long> {
    List<Food> findByCategoryId(Long categoryId);

    List<Food> findByRestaurantId(Long restaurantId);

    @Query("SELECT f FROM Food f WHERE LOWER(f.name) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<Food> searchByKeyword(@Param("keyword") String keyword);
}

@Repository
@RequiredArgsConstructor
public class FoodRepositoryImpl implements FoodRepository {

    private final JpaFoodRepository jpaFoodRepository;

    @Override
    public Food save(Food food) {
        return jpaFoodRepository.save(food);
    }

    @Override
    public Food findById(Long id) {
        return jpaFoodRepository.findById(id).orElse(null);
    }

    @Override
    public List<Food> findByCategoryId(Long categoryId) {
        return jpaFoodRepository.findByCategoryId(categoryId);
    }

    @Override
    public List<Food> findByRestaurantId(Long restaurantId) {
        return jpaFoodRepository.findByRestaurantId(restaurantId);
    }

    @Override
    public List<Food> searchByName(String keyword) {
        if (keyword == null || keyword.isBlank()) {
            return jpaFoodRepository.findAll();
        }
        return jpaFoodRepository.searchByKeyword(keyword);
    }
}
