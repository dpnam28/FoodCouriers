package org.dpnam28.foodcouriers.infrastructure.repository;

import lombok.RequiredArgsConstructor;
import org.dpnam28.foodcouriers.domain.entity.Order;
import org.dpnam28.foodcouriers.domain.repository.OrderRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

interface JpaOrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByRestaurant_Id(Long restaurantId);

    List<Order> findByCustomer_Id(Long customerId);

    List<Order> findByCourier_Id(Long courierId);
}

@Repository
@RequiredArgsConstructor
public class OrderRepositoryImpl implements OrderRepository {

    private final JpaOrderRepository jpaOrderRepository;

    @Override
    public Order save(Order order) {
        return jpaOrderRepository.save(order);
    }

    @Override
    public Order findById(Long id) {
        return jpaOrderRepository.findById(id).orElse(null);
    }

    @Override
    public List<Order> findByRestaurantId(Long restaurantId) {
        return jpaOrderRepository.findByRestaurant_Id(restaurantId);
    }

    @Override
    public List<Order> findByCustomerId(Long customerId) {
        return jpaOrderRepository.findByCustomer_Id(customerId);
    }

    @Override
    public List<Order> findByCourierId(Long courierId) {
        return jpaOrderRepository.findByCourier_Id(courierId);
    }
}
