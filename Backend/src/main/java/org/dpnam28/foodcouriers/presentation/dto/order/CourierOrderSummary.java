package org.dpnam28.foodcouriers.presentation.dto.order;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CourierOrderSummary {
    private Long id;
    private String fullName;
    private String phoneNumber;
    private Boolean isAvailable;
}
