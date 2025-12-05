package org.dpnam28.foodcouriers.usecase;

import lombok.RequiredArgsConstructor;
import org.dpnam28.foodcouriers.domain.entity.Location;
import org.dpnam28.foodcouriers.domain.repository.LocationRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LocationUseCase {
    private final LocationRepository locationRepository;

    public List<Location> getAll() {
        return locationRepository.getAll();
    }

    public Location update(Long id, Location location) {
        return locationRepository.update(id, location);
    }
}
