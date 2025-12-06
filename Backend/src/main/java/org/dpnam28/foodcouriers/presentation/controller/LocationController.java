package org.dpnam28.foodcouriers.presentation.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dpnam28.foodcouriers.domain.dto.ApiResponse;
import org.dpnam28.foodcouriers.domain.entity.Location;
import org.dpnam28.foodcouriers.presentation.dto.location.LocationResponse;
import org.dpnam28.foodcouriers.presentation.dto.location.LocationUpdateRequest;
import org.dpnam28.foodcouriers.presentation.mapper.LocationMapper;
import org.dpnam28.foodcouriers.usecase.LocationUseCase;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/locations")
@RequiredArgsConstructor
public class LocationController {
    private final LocationUseCase locationUseCase;
    private final LocationMapper locationMapper;

    @GetMapping
    public ApiResponse<List<LocationResponse>> getAll() {
        List<LocationResponse> locations =
                locationMapper.toListLocationResponse(locationUseCase.getAll());
        return ApiResponse.apiResponseSuccess("Get all locations succeeded", locations);
    }

    @PutMapping("/{id}")
    public ApiResponse<LocationResponse> update(@PathVariable Long id,
                                                @RequestBody @Valid LocationUpdateRequest request) {
        Location location = locationMapper.toLocation(request);
        Location locationUpdated = locationUseCase.update(id, location);
        LocationResponse locationResponse = locationMapper.toLocationResponse(locationUpdated);
        return ApiResponse.apiResponseSuccess("Update location succeeded", locationResponse);
    }
}
