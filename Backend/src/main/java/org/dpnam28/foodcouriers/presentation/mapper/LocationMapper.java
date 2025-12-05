package org.dpnam28.foodcouriers.presentation.mapper;

import org.dpnam28.foodcouriers.domain.entity.Location;
import org.dpnam28.foodcouriers.presentation.dto.location.LocationResponse;
import org.dpnam28.foodcouriers.presentation.dto.location.LocationUpdateRequest;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface LocationMapper {
    List<LocationResponse> toListLocationResponse(List<Location> location);
    LocationResponse toLocationResponse(Location location);
    Location toLocation(LocationUpdateRequest location);
}
