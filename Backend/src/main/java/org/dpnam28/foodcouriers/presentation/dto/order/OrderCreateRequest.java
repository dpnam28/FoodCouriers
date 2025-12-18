package org.dpnam28.foodcouriers.presentation.dto.order;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.dpnam28.foodcouriers.domain.entity.OrderStatus;

import java.util.List;

@Data
public class OrderCreateRequest {

    @NotEmpty(message = "Danh sách cart item là bắt buộc")
    private List<Long> cartItemIds;

    private Long courierId;

    @NotNull(message = "customerId là bắt buộc")
    private Long customerId;

    @NotNull(message = "restaurantId là bắt buộc")
    private Long restaurantId;

    private OrderStatus status = OrderStatus.PENDING;
}
