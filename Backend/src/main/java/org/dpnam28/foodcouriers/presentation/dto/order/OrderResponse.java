package org.dpnam28.foodcouriers.presentation.dto.order;

import lombok.Builder;
import lombok.Data;
import org.dpnam28.foodcouriers.domain.entity.OrderStatus;
import org.dpnam28.foodcouriers.presentation.dto.restaurant.RestaurantResponse;

import java.util.List;

@Data
@Builder
public class OrderResponse {
    private Long id;
    private Double totalPrice;
    private OrderStatus status;
    private RestaurantResponse restaurant;
    private CustomerOrderSummary customer;
    private CourierOrderSummary courier;
    private List<OrderDetailResponse> orderDetails;
}
