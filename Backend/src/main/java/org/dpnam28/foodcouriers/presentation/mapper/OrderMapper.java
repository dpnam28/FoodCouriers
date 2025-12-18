package org.dpnam28.foodcouriers.presentation.mapper;

import lombok.RequiredArgsConstructor;
import org.dpnam28.foodcouriers.domain.entity.Courier;
import org.dpnam28.foodcouriers.domain.entity.Customer;
import org.dpnam28.foodcouriers.domain.entity.Order;
import org.dpnam28.foodcouriers.domain.entity.OrderDetail;
import org.dpnam28.foodcouriers.presentation.dto.order.*;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class OrderMapper {

    private final FoodMapper foodMapper;
    private final RestaurantMapper restaurantMapper;

    public OrderResponse toResponse(Order order) {
        if (order == null) {
            return null;
        }
        return OrderResponse.builder()
                .id(order.getId())
                .totalPrice(order.getTotalPrice())
                .status(order.getStatus())
                .restaurant(restaurantMapper.toRestaurantResponse(order.getRestaurant()))
                .customer(toCustomerSummary(order.getCustomer()))
                .courier(toCourierSummary(order.getCourier()))
                .orderDetails(toDetailResponses(order.getOrderDetails()))
                .build();
    }

    public List<OrderResponse> toResponses(List<Order> orders) {
        if (orders == null) {
            return Collections.emptyList();
        }
        return orders.stream().map(this::toResponse).collect(Collectors.toList());
    }

    private CustomerOrderSummary toCustomerSummary(Customer customer) {
        if (customer == null || customer.getUser() == null) {
            return null;
        }
        return CustomerOrderSummary.builder()
                .id(customer.getId())
                .fullName(customer.getUser().getFullName())
                .phoneNumber(customer.getUser().getPhoneNumber())
                .address(customer.getUser().getAddress())
                .build();
    }

    private CourierOrderSummary toCourierSummary(Courier courier) {
        if (courier == null || courier.getUser() == null) {
            return null;
        }
        return CourierOrderSummary.builder()
                .id(courier.getId())
                .fullName(courier.getUser().getFullName())
                .phoneNumber(courier.getUser().getPhoneNumber())
                .isAvailable(courier.getIsAvailable())
                .build();
    }

    private List<OrderDetailResponse> toDetailResponses(List<OrderDetail> details) {
        if (details == null) {
            return Collections.emptyList();
        }
        return details.stream()
                .map(detail -> OrderDetailResponse.builder()
                        .id(detail.getId())
                        .quantity(detail.getQuantity())
                        .totalPrice(detail.getTotalPrice())
                        .food(foodMapper.toFoodResponse(detail.getFood()))
                        .build())
                .collect(Collectors.toList());
    }
}
