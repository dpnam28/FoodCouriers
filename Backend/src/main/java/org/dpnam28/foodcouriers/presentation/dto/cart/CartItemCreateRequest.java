package org.dpnam28.foodcouriers.presentation.dto.cart;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CartItemCreateRequest {

    @NotNull(message = "userId là bắt buộc")
    private Long userId;

    @NotNull(message = "foodId là bắt buộc")
    private Long foodId;

    @Min(value = 1, message = "Số lượng phải lớn hơn hoặc bằng 1")
    private Integer quantity;
}
