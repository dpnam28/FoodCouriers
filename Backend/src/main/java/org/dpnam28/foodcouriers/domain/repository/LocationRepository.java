package org.dpnam28.foodcouriers.domain.repository;

import org.dpnam28.foodcouriers.domain.entity.Location;

import java.util.List;
import java.util.Optional;

public interface LocationRepository {
    List<Location> getAll();
    Location update(Long id, Location location);
    Optional<Location> findById(Long id);
}
