package org.dpnam28.foodcouriers.presentation.dto.cart;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CartItemResponse {
    private Long id;
    private Long userId;
    private String userName;
    private Long foodId;
    private String foodName;
    private Long restaurantId;
    private String restaurantName;
    private Integer quantity;
    private Double totalPrice;
}
