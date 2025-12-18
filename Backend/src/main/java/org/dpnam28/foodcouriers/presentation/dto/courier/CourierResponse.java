package org.dpnam28.foodcouriers.presentation.dto.courier;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CourierResponse {
    private Long id;
    private String fullName;
    private String phoneNumber;
    private String location;
    private Boolean isAvailable;
}
