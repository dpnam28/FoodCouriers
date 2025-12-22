package org.dpnam28.foodcouriers.usecase;

import lombok.RequiredArgsConstructor;
import org.dpnam28.foodcouriers.domain.entity.*;
import org.dpnam28.foodcouriers.domain.exception.AppException;
import org.dpnam28.foodcouriers.domain.exception.ErrorCode;
import org.dpnam28.foodcouriers.domain.repository.*;
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

    public Order createOrder(Order order, List<Long> cartItemIds) {
        Long restaurantId = order != null && order.getRestaurant() != null ? order.getRestaurant().getId() : null;
        Long customerId = order != null && order.getCustomer() != null ? order.getCustomer().getId() : null;
        Long courierId = order != null && order.getCourier() != null ? order.getCourier().getId() : null;

        Restaurant restaurant = restaurantRepository.findById(restaurantId);
        if (restaurant == null) {
            throw new AppException(ErrorCode.RESTAURANT_NOT_FOUND);
        }
        Customer customer = customerRepository.findById(customerId);
        if (customer == null) {
            throw new AppException(ErrorCode.CUSTOMER_NOT_FOUND);
        }
        Courier courier = null;
        if (courierId != null) {
            courier = courierRepository.findById(courierId);
            if (courier == null) {
                throw new AppException(ErrorCode.COURIER_NOT_FOUND);
            }
        }

        if (cartItemIds == null || cartItemIds.isEmpty()) {
            throw new AppException(ErrorCode.CART_ITEM_NOT_FOUND);
        }
        List<CartItem> cartItems = cartItemRepository.findByIds(cartItemIds);
        if (cartItems.isEmpty() || cartItems.size() != cartItemIds.size()) {
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
        assert order != null;
        Order newOrder = Order.builder()
                .restaurant(restaurant)
                .customer(customer)
                .courier(courier)
                .status(order.getStatus() == null ? OrderStatus.PENDING : order.getStatus())
                .totalPrice(itemsTotal)
                .build();

        List<OrderDetail> details = new ArrayList<>();
        for (CartItem cartItem : cartItems) {
            OrderDetail detail = OrderDetail.builder()
                    .order(newOrder)
                    .food(cartItem.getFood())
                    .quantity(cartItem.getQuantity())
                    .totalPrice(cartItem.getTotalPrice())
                    .build();
            details.add(detail);
        }
        newOrder.setOrderDetails(details);

        return orderRepository.save(newOrder);
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
        boolean authorized = order.getCustomer() != null
                && order.getCustomer().getId().equals(customerId);
        if (order.getRestaurant() != null
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
