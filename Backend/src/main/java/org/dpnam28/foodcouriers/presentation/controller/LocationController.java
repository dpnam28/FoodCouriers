package org.dpnam28.foodcouriers.presentation.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dpnam28.foodcouriers.domain.dto.ApiResponse;
import org.dpnam28.foodcouriers.presentation.dto.location.LocationResponse;
import org.dpnam28.foodcouriers.presentation.mapper.LocationMapper;
import org.dpnam28.foodcouriers.usecase.LocationUseCase;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
}
