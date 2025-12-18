package org.dpnam28.foodcouriers.domain.repository;

import org.dpnam28.foodcouriers.domain.entity.CartItem;

import java.util.List;

public interface CartItemRepository {
    CartItem save(CartItem cartItem);

    CartItem findById(Long id);

    void deleteById(Long id);

    List<CartItem> findByUserId(Long userId);

    List<CartItem> findByIds(List<Long> ids);

    void deleteByUserId(Long userId);
}
