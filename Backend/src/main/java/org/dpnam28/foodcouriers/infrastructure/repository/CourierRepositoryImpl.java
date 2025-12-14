package org.dpnam28.foodcouriers.infrastructure.repository;

import lombok.RequiredArgsConstructor;
import org.dpnam28.foodcouriers.domain.entity.Courier;
import org.dpnam28.foodcouriers.domain.repository.CourierRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

interface JpaCourierRepository extends JpaRepository<Courier, Long> {
}

@Repository
@RequiredArgsConstructor
public class CourierRepositoryImpl implements CourierRepository {

    private final JpaCourierRepository jpaCourierRepository;
    @Override
    public Courier save(Courier courier) {
        return jpaCourierRepository.save(courier);
    }
}
