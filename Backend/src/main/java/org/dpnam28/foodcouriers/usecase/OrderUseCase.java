package org.dpnam28.foodcouriers.usecase;

import lombok.RequiredArgsConstructor;
import org.dpnam28.foodcouriers.domain.entity.*;
import org.dpnam28.foodcouriers.domain.exception.AppException;
import org.dpnam28.foodcouriers.domain.exception.ErrorCode;
import org.dpnam28.foodcouriers.domain.repository.*;
import org.dpnam28.foodcouriers.presentation.dto.order.OrderCreateRequest;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderUseCase {

    private final OrderRepository orderRepository;
    private final RestaurantRepository restaurantRepository;
    private final CustomerRepository customerRepository;
    private final CourierRepository courierRepository;
    private final CartItemRepository cartItemRepository;

    public Order createOrder(OrderCreateRequest request) {
        Restaurant restaurant = restaurantRepository.findById(request.getRestaurantId());
        if (restaurant == null) {
            throw new AppException(ErrorCode.RESTAURANT_NOT_FOUND);
        }
        Customer customer = customerRepository.findById(request.getCustomerId());
        if (customer == null) {
            throw new AppException(ErrorCode.CUSTOMER_NOT_FOUND);
        }
        Courier courier = null;
        if (request.getCourierId() != null) {
            courier = courierRepository.findById(request.getCourierId());
            if (courier == null) {
                throw new AppException(ErrorCode.COURIER_NOT_FOUND);
            }
        }

        List<CartItem> cartItems = cartItemRepository.findByIds(request.getCartItemIds());
        if (cartItems.isEmpty() || cartItems.size() != request.getCartItemIds().size()) {
            throw new AppException(ErrorCode.CART_ITEM_NOT_FOUND);
        }

        Long customerUserId = customer.getUser() != null ? customer.getUser().getId() : null;
        for (CartItem cartItem : cartItems) {
            Long cartUserId = cartItem.getUser() != null ? cartItem.getUser().getId() : null;
            if (customerUserId != null && cartUserId != null && !cartUserId.equals(customerUserId)) {
                throw new AppException(ErrorCode.USER_NOT_OWN_CART_ITEM);
            }
            if (cartItem.getFood() == null || cartItem.getFood().getRestaurant() == null
                    || !cartItem.getFood().getRestaurant().getId().equals(restaurant.getId())) {
                throw new AppException(ErrorCode.RESTAURANT_NOT_OWN_ITEM);
            }
        }

        double itemsTotal = cartItems.stream()
                .mapToDouble(item -> item.getTotalPrice() != null ? item.getTotalPrice() : 0.0)
                .sum();
        double deliveryFee = restaurant.getDeliveryFee() != null ? restaurant.getDeliveryFee() : 0.0;
        double finalTotal = itemsTotal + deliveryFee;

        Order order = Order.builder()
                .restaurant(restaurant)
                .customer(customer)
                .courier(courier)
                .status(request.getStatus() == null ? OrderStatus.PENDING : request.getStatus())
                .totalPrice(finalTotal)
                .build();

        List<OrderDetail> details = new ArrayList<>();
        for (CartItem cartItem : cartItems) {
            OrderDetail detail = OrderDetail.builder()
                    .order(order)
                    .food(cartItem.getFood())
                    .quantity(cartItem.getQuantity())
                    .totalPrice(cartItem.getTotalPrice())
                    .build();
            details.add(detail);
        }
        order.setOrderDetails(details);

        return orderRepository.save(order);
    }

    public List<Order> getOrdersForRestaurant(Long restaurantId) {
        Restaurant restaurant = restaurantRepository.findById(restaurantId);
        if (restaurant == null) {
            throw new AppException(ErrorCode.RESTAURANT_NOT_FOUND);
        }
        return orderRepository.findByRestaurantId(restaurantId);
    }

    public List<Order> getOrdersForCustomer(Long customerId) {
        Customer customer = customerRepository.findById(customerId);
        if (customer == null) {
            throw new AppException(ErrorCode.CUSTOMER_NOT_FOUND);
        }
        return orderRepository.findByCustomerId(customerId);
    }

    public Order updateStatusByRestaurant(Long orderId, Long restaurantId, OrderStatus status) {
        Order order = getOrderOrThrow(orderId);
        if (order.getRestaurant() == null || !order.getRestaurant().getId().equals(restaurantId)) {
            throw new AppException(ErrorCode.UNAUTHORIZED_ACTION);
        }
        order.setStatus(status);
        return orderRepository.save(order);
    }

    public Order updateStatusByCourier(Long orderId, Long courierId, OrderStatus status) {
        Order order = getOrderOrThrow(orderId);
        if (order.getCourier() == null || !order.getCourier().getId().equals(courierId)) {
            throw new AppException(ErrorCode.UNAUTHORIZED_ACTION);
        }
        order.setStatus(status);
        return orderRepository.save(order);
    }

    public Order cancelOrder(Long orderId, Long customerId, Long restaurantId) {
        Order order = getOrderOrThrow(orderId);
        boolean authorized = false;
        if (customerId != null && order.getCustomer() != null
                && order.getCustomer().getId().equals(customerId)) {
            authorized = true;
        }
        if (restaurantId != null && order.getRestaurant() != null
                && order.getRestaurant().getId().equals(restaurantId)) {
            authorized = true;
        }
        if (!authorized) {
            throw new AppException(ErrorCode.UNAUTHORIZED_ACTION);
        }
        order.setStatus(OrderStatus.CANCELED);
        return orderRepository.save(order);
    }

    public Order assignCourier(Long orderId, Long restaurantId, Long courierId) {
        Order order = getOrderOrThrow(orderId);
        if (order.getRestaurant() == null || !order.getRestaurant().getId().equals(restaurantId)) {
            throw new AppException(ErrorCode.UNAUTHORIZED_ACTION);
        }
        Courier courier = courierRepository.findById(courierId);
        if (courier == null) {
            throw new AppException(ErrorCode.COURIER_NOT_FOUND);
        }
        order.setCourier(courier);
        order.setStatus(OrderStatus.ACCEPTED);
        return orderRepository.save(order);
    }

    public List<Order> getOrdersForCourier(Long courierId) {
        Courier courier = courierRepository.findById(courierId);
        if (courier == null) {
            throw new AppException(ErrorCode.COURIER_NOT_FOUND);
        }
        return orderRepository.findByCourierId(courierId);
    }

    public List<Courier> getCouriersByLocation(String location) {
        return courierRepository.findByLocation(location);
    }

    private Order getOrderOrThrow(Long orderId) {
        Order order = orderRepository.findById(orderId);
        if (order == null) {
            throw new AppException(ErrorCode.ORDER_NOT_FOUND);
        }
        return order;
    }
}
