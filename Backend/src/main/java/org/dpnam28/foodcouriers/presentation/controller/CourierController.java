package org.dpnam28.foodcouriers.presentation.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.dpnam28.foodcouriers.domain.dto.ApiResponse;
import org.dpnam28.foodcouriers.presentation.dto.courier.CourierAvailabilityRequest;
import org.dpnam28.foodcouriers.presentation.dto.courier.CourierAvailabilityResponse;
import org.dpnam28.foodcouriers.usecase.CourierUseCase;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/couriers")
@RequiredArgsConstructor
public class CourierController {

    private final CourierUseCase courierUseCase;

    @PutMapping("/{id}/availability")
    public ApiResponse<CourierAvailabilityResponse> setAvailable(
            @PathVariable Long id,
            @RequestBody @Valid CourierAvailabilityRequest request
    ) {
        Boolean available = courierUseCase.setAvailable(id, request.getAvailable());
        return ApiResponse.apiResponseSuccess(
                "Update courier availability succeeded",
                new CourierAvailabilityResponse(available)
        );
    }
}
