package org.dpnam28.foodcouriers.usecase;

import lombok.RequiredArgsConstructor;
import org.dpnam28.foodcouriers.domain.entity.CartItem;
import org.dpnam28.foodcouriers.domain.entity.Food;
import org.dpnam28.foodcouriers.domain.entity.User;
import org.dpnam28.foodcouriers.domain.exception.AppException;
import org.dpnam28.foodcouriers.domain.exception.ErrorCode;
import org.dpnam28.foodcouriers.domain.repository.CartItemRepository;
import org.dpnam28.foodcouriers.domain.repository.FoodRepository;
import org.dpnam28.foodcouriers.domain.repository.UserRepository;
import org.dpnam28.foodcouriers.presentation.dto.cart.CartItemCreateRequest;
import org.dpnam28.foodcouriers.presentation.dto.cart.CartItemUpdateRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CartItemUseCase {

    private final CartItemRepository cartItemRepository;
    private final UserRepository userRepository;
    private final FoodRepository foodRepository;

    public CartItem addCartItem(CartItemCreateRequest request) {
        User user = userRepository.findById(request.getUserId());
        if (user == null) {
            throw new AppException(ErrorCode.USER_NOT_FOUND);
        }
        Food food = foodRepository.findById(request.getFoodId());
        if (food == null) {
            throw new AppException(ErrorCode.FOOD_NOT_FOUND);
        }
        int quantity = (request.getQuantity() == null || request.getQuantity() < 1) ? 1 : request.getQuantity();
        double totalPrice = quantity * food.getPrice();
        CartItem cartItem = CartItem.builder()
                .user(user)
                .food(food)
                .quantity(quantity)
                .totalPrice(totalPrice)
                .build();
        return cartItemRepository.save(cartItem);
    }

    public void deleteCartItem(Long id) {
        CartItem cartItem = getCartItemOrThrow(id);
        cartItemRepository.deleteById(cartItem.getId());
    }

    public CartItem updateQuantity(Long id, CartItemUpdateRequest request) {
        CartItem cartItem = getCartItemOrThrow(id);
        int quantity = request.getQuantity();
        cartItem.setQuantity(quantity);
        cartItem.setTotalPrice(quantity * cartItem.getFood().getPrice());
        return cartItemRepository.save(cartItem);
    }

    public List<CartItem> getCartItemsByUserId(Long userId) {
        if (userId == null) {
            throw new AppException(ErrorCode.ARGUMENT_IS_REQUIRED, ErrorCode.ARGUMENT_IS_REQUIRED.formatMessage("ID người dùng"));
        }
        User user = userRepository.findById(userId);
        if (user == null) {
            throw new AppException(ErrorCode.USER_NOT_FOUND);
        }
        return cartItemRepository.findByUserId(userId);
    }

    @Transactional
    public void deleteCartItemsByUserId(Long userId) {
        if (userId == null) {
            throw new AppException(ErrorCode.ARGUMENT_IS_REQUIRED, ErrorCode.ARGUMENT_IS_REQUIRED.formatMessage("ID người dùng"));
        }
        User user = userRepository.findById(userId);
        if (user == null) {
            throw new AppException(ErrorCode.USER_NOT_FOUND);
        }
        cartItemRepository.deleteByUserId(userId);
    }

    private CartItem getCartItemOrThrow(Long id) {
        CartItem cartItem = cartItemRepository.findById(id);
        if (cartItem == null) {
            throw new AppException(ErrorCode.CART_ITEM_NOT_FOUND);
        }
        return cartItem;
    }
}
