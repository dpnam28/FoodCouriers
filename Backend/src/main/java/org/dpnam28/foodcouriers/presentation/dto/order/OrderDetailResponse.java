package org.dpnam28.foodcouriers.presentation.dto.order;

import lombok.Builder;
import lombok.Data;
import org.dpnam28.foodcouriers.presentation.dto.food.FoodResponse;

@Data
@Builder
public class OrderDetailResponse {
    private Long id;
    private Integer quantity;
    private Double totalPrice;
    private FoodResponse food;
}
