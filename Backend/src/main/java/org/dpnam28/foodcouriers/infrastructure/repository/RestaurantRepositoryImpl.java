package org.dpnam28.foodcouriers.infrastructure.repository;

import lombok.RequiredArgsConstructor;
import org.dpnam28.foodcouriers.domain.entity.Restaurant;
import org.dpnam28.foodcouriers.domain.repository.RestaurantRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

interface JpaRestaurantRepository extends JpaRepository<Restaurant, Long> {
    @Query("SELECT r FROM Restaurant r " +
            "WHERE (:name IS NULL OR LOWER(r.user.fullName) LIKE LOWER(CONCAT('%', :name, '%'))) " +
            "AND (:location IS NULL OR LOWER(r.user.location.city) LIKE LOWER(CONCAT('%', :location, '%')))")
    List<Restaurant> searchByFilters(@Param("name") String name, @Param("location") String location);
}

@Repository
@RequiredArgsConstructor
public class RestaurantRepositoryImpl implements RestaurantRepository {
    private final JpaRestaurantRepository jpaRestaurantRepository;

    @Override
    public void save(Restaurant restaurant) {
        jpaRestaurantRepository.save(restaurant);
    }

    @Override
    public Restaurant findById(Long id) {
        return jpaRestaurantRepository.findById(id)
                .orElse(null);
    }

    @Override
    public List<Restaurant> searchByNameAndLocation(String name, String location) {
        String nameFilter = (name == null || name.isBlank()) ? null : name;
        String locationFilter = (location == null || location.isBlank()) ? null : location;
        return jpaRestaurantRepository.searchByFilters(nameFilter, locationFilter);
    }
}
