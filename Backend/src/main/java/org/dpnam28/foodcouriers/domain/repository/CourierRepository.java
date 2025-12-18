package org.dpnam28.foodcouriers.domain.repository;

import org.dpnam28.foodcouriers.domain.entity.Courier;

public interface CourierRepository {
    Courier save(Courier courier);
    Courier findById(Long id);

    java.util.List<Courier> findByLocation(String location);
}
