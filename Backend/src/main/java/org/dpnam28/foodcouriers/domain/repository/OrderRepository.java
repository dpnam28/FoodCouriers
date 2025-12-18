package org.dpnam28.foodcouriers.domain.repository;

import org.dpnam28.foodcouriers.domain.entity.Order;

import java.util.List;

public interface OrderRepository {
    Order save(Order order);

    Order findById(Long id);

    List<Order> findByRestaurantId(Long restaurantId);

    List<Order> findByCustomerId(Long customerId);

    List<Order> findByCourierId(Long courierId);
}
