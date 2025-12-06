package org.dpnam28.foodcouriers.infrastructure.repository;

import lombok.RequiredArgsConstructor;
import org.dpnam28.foodcouriers.domain.entity.Location;
import org.dpnam28.foodcouriers.domain.exception.AppException;
import org.dpnam28.foodcouriers.domain.exception.ErrorCode;
import org.dpnam28.foodcouriers.domain.repository.LocationRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

interface JpaLocationRepository extends JpaRepository<Location, Long> {
}

@Repository
@RequiredArgsConstructor
public class LocationRepositoryImpl implements LocationRepository {

    private final JpaLocationRepository jpaLocationRepository;

    @Override
    public List<Location> getAll() {
        return jpaLocationRepository.findAll();
    }

    @Override
    public Location update(Long id, Location location) {
        Location locationToUpdate = jpaLocationRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.LOCATION_NOT_FOUND));
        locationToUpdate.setCity(location.getCity());
        return jpaLocationRepository.save(locationToUpdate);
    }

    @Override
    public Optional<Location> findById(Long id) {
        return jpaLocationRepository.findById(id);
    }
}
