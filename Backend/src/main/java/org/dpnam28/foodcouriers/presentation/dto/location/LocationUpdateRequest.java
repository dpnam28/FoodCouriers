package org.dpnam28.foodcouriers.presentation.dto.location;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LocationUpdateRequest {

    @NotBlank(message = "Thành phố là bắt buộc")
    private String city;
}
