package org.dpnam28.foodcouriers.presentation.dto.courier;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CourierAvailabilityRequest {
    @NotNull(message = "available is required")
    private Boolean available;
}
