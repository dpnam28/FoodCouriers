package org.dpnam28.foodcouriers.presentation.dto.customer;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomerTotalOrdersRequest {
    @NotNull(message = "totalOrders is required")
    private Integer totalOrders;
}
