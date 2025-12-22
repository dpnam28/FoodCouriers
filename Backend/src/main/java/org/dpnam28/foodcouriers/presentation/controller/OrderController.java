package org.dpnam28.foodcouriers.presentation.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.dpnam28.foodcouriers.domain.dto.ApiResponse;
import org.dpnam28.foodcouriers.domain.entity.Order;
import org.dpnam28.foodcouriers.domain.entity.OrderStatus;
import org.dpnam28.foodcouriers.presentation.dto.courier.CourierLocationRequest;
import org.dpnam28.foodcouriers.presentation.dto.courier.CourierResponse;
import org.dpnam28.foodcouriers.presentation.dto.order.OrderCreateRequest;
import org.dpnam28.foodcouriers.presentation.dto.order.OrderResponse;
import org.dpnam28.foodcouriers.presentation.mapper.OrderMapper;
import org.dpnam28.foodcouriers.usecase.OrderUseCase;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderUseCase orderUseCase;
    private final OrderMapper orderMapper;

    @PostMapping
    public ApiResponse<OrderResponse> createOrder(@Valid @RequestBody OrderCreateRequest request) {
        Order orderRequest = orderMapper.toOrder(request);
        Order order = orderUseCase.createOrder(orderRequest, request.getCartItemIds());
        return ApiResponse.apiResponseSuccess("Tạo đơn hàng thành công", orderMapper.toResponse(order));
    }

    @GetMapping("/restaurant/{restaurantId}")
    public ApiResponse<List<OrderResponse>> getOrdersForRestaurant(@PathVariable Long restaurantId) {
        List<Order> orders = orderUseCase.getOrdersForRestaurant(restaurantId);
        return ApiResponse.apiResponseSuccess("Lấy đơn hàng theo nhà hàng thành công", orderMapper.toResponses(orders));
    }

    @GetMapping("/customer/{customerId}")
    public ApiResponse<List<OrderResponse>> getOrdersForCustomer(@PathVariable Long customerId) {
        List<Order> orders = orderUseCase.getOrdersForCustomer(customerId);
        return ApiResponse.apiResponseSuccess("Lấy đơn hàng theo khách hàng thành công", orderMapper.toResponses(orders));
    }

    @PatchMapping("/{orderId}/accept")
    public ApiResponse<OrderResponse> acceptOrder(@PathVariable Long orderId, @RequestParam Long restaurantId) {
        Order order = orderUseCase.updateStatusByRestaurant(orderId, restaurantId, OrderStatus.ACCEPTED);
        return ApiResponse.apiResponseSuccess("Nhà hàng đã xác nhận đơn hàng", orderMapper.toResponse(order));
    }

    @PatchMapping("/{orderId}/deliver")
    public ApiResponse<OrderResponse> completeDelivery(@PathVariable Long orderId, @RequestParam Long courierId) {
        Order order = orderUseCase.updateStatusByCourier(orderId, courierId, OrderStatus.DELIVERED);
        return ApiResponse.apiResponseSuccess("Đơn hàng đã được giao thành công", orderMapper.toResponse(order));
    }

    @PatchMapping("/{orderId}/cancel")
    public ApiResponse<OrderResponse> cancelOrder(
            @PathVariable Long orderId,
            @RequestParam(required = false) Long customerId,
            @RequestParam(required = false) Long restaurantId) {
        Order order = orderUseCase.cancelOrder(orderId, customerId, restaurantId);
        return ApiResponse.apiResponseSuccess("Đơn hàng đã được hủy", orderMapper.toResponse(order));
    }

    @PatchMapping("/{orderId}/assign-courier")
    public ApiResponse<OrderResponse> assignCourier(
            @PathVariable Long orderId,
            @RequestParam Long restaurantId,
            @RequestParam Long courierId) {
        Order order = orderUseCase.assignCourier(orderId, restaurantId, courierId);
        return ApiResponse.apiResponseSuccess("Đơn hàng đã được giao cho shipper", orderMapper.toResponse(order));
    }

    @GetMapping("/courier/{courierId}")
    public ApiResponse<List<OrderResponse>> getOrdersForCourier(@PathVariable Long courierId) {
        List<Order> orders = orderUseCase.getOrdersForCourier(courierId);
        return ApiResponse.apiResponseSuccess("Lấy đơn hàng theo shipper thành công", orderMapper.toResponses(orders));
    }

    @PostMapping("/couriers/by-location")
    public ApiResponse<List<CourierResponse>> getCouriersByLocation(@RequestBody CourierLocationRequest request) {
        List<CourierResponse> couriers = orderUseCase.getCouriersByLocation(request.getLocation()).stream()
                .map(courier -> CourierResponse.builder()
                        .id(courier.getId())
                        .fullName(courier.getUser() != null ? courier.getUser().getFullName() : null)
                        .phoneNumber(courier.getUser() != null ? courier.getUser().getPhoneNumber() : null)
                        .location(courier.getUser() != null && courier.getUser().getLocation() != null
                                ? courier.getUser().getLocation().getCity() : null)
                        .isAvailable(courier.getIsAvailable())
                        .build())
                .collect(Collectors.toList());
        return ApiResponse.apiResponseSuccess("Lấy danh sách shipper thành công", couriers);
    }
}
