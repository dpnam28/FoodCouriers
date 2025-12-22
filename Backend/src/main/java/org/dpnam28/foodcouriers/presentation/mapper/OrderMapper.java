package org.dpnam28.foodcouriers.presentation.mapper;

import org.dpnam28.foodcouriers.domain.entity.Courier;
import org.dpnam28.foodcouriers.domain.entity.Customer;
import org.dpnam28.foodcouriers.domain.entity.Order;
import org.dpnam28.foodcouriers.domain.entity.OrderDetail;
import org.dpnam28.foodcouriers.presentation.dto.order.CustomerOrderSummary;
import org.dpnam28.foodcouriers.presentation.dto.order.CourierOrderSummary;
import org.dpnam28.foodcouriers.presentation.dto.order.OrderCreateRequest;
import org.dpnam28.foodcouriers.presentation.dto.order.OrderDetailResponse;
import org.dpnam28.foodcouriers.presentation.dto.order.OrderResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring", uses = {FoodMapper.class, RestaurantMapper.class})
public interface OrderMapper {

    @Mapping(target = "restaurant", source = "restaurant")
    @Mapping(target = "customer", expression = "java(toCustomerSummary(order.getCustomer()))")
    @Mapping(target = "courier", expression = "java(toCourierSummary(order.getCourier()))")
    OrderResponse toResponse(Order order);

    List<OrderResponse> toResponses(List<Order> orders);

    @Mapping(target = "food", source = "food")
    OrderDetailResponse toOrderDetailResponse(OrderDetail detail);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "totalPrice", ignore = true)
    @Mapping(target = "orderDetails", ignore = true)
    @Mapping(target = "status", source = "status")
    @Mapping(target = "restaurant.id", source = "restaurantId")
    @Mapping(target = "customer.id", source = "customerId")
    @Mapping(target = "courier.id", source = "courierId")
    Order toOrder(OrderCreateRequest request);

    default CustomerOrderSummary toCustomerSummary(Customer customer) {
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

    default CourierOrderSummary toCourierSummary(Courier courier) {
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
}
