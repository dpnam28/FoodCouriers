package org.dpnam28.foodcouriers.domain.repository;

import org.dpnam28.foodcouriers.domain.entity.Location;

import java.util.List;

public interface LocationRepository {
    List<Location> getAll();
    Location findById(Long id);
}
