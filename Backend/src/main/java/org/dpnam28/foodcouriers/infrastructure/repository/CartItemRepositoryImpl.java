package org.dpnam28.foodcouriers.infrastructure.repository;

import lombok.RequiredArgsConstructor;
import org.dpnam28.foodcouriers.domain.entity.CartItem;
import org.dpnam28.foodcouriers.domain.repository.CartItemRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

interface JpaCartItemRepository extends JpaRepository<CartItem, Long> {
    List<CartItem> findByUser_Id(Long userId);

    List<CartItem> findByIdIn(List<Long> ids);
}

@Repository
@RequiredArgsConstructor
public class CartItemRepositoryImpl implements CartItemRepository {

    private final JpaCartItemRepository jpaCartItemRepository;

    @Override
    public CartItem save(CartItem cartItem) {
        return jpaCartItemRepository.save(cartItem);
    }

    @Override
    public CartItem findById(Long id) {
        return jpaCartItemRepository.findById(id).orElse(null);
    }

    @Override
    public void deleteById(Long id) {
        jpaCartItemRepository.deleteById(id);
    }

    @Override
    public List<CartItem> findByUserId(Long userId) {
        if (userId == null) {
            return List.of();
        }
        return jpaCartItemRepository.findByUser_Id(userId);
    }

    @Override
    public List<CartItem> findByIds(List<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            return List.of();
        }
        return jpaCartItemRepository.findByIdIn(ids);
    }
}
