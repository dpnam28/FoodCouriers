package org.dpnam28.foodcouriers.presentation.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.dpnam28.foodcouriers.domain.dto.ApiResponse;
import org.dpnam28.foodcouriers.domain.entity.CartItem;
import org.dpnam28.foodcouriers.presentation.dto.cart.CartItemCreateRequest;
import org.dpnam28.foodcouriers.presentation.dto.cart.CartItemResponse;
import org.dpnam28.foodcouriers.presentation.dto.cart.CartItemUpdateRequest;
import org.dpnam28.foodcouriers.presentation.mapper.CartItemMapper;
import org.dpnam28.foodcouriers.usecase.CartItemUseCase;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/cart-items")
@RequiredArgsConstructor
public class CartItemController {

    private final CartItemUseCase cartItemUseCase;
    private final CartItemMapper cartItemMapper;

    @PostMapping
    public ApiResponse<CartItemResponse> addCartItem(@Valid @RequestBody CartItemCreateRequest request) {
        CartItem cartItem = cartItemUseCase.addCartItem(request);
        return ApiResponse.apiResponseSuccess("Thêm sản phẩm vào giỏ hàng thành công", cartItemMapper.toResponse(cartItem));
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteCartItem(@PathVariable Long id) {
        cartItemUseCase.deleteCartItem(id);
        return ApiResponse.apiResponseSuccess("Xóa sản phẩm khỏi giỏ hàng thành công", null);
    }

    @PutMapping("/{id}")
    public ApiResponse<CartItemResponse> updateCartItem(@PathVariable Long id,
                                                        @Valid @RequestBody CartItemUpdateRequest request) {
        CartItem cartItem = cartItemUseCase.updateQuantity(id, request);
        return ApiResponse.apiResponseSuccess("Cập nhật giỏ hàng thành công", cartItemMapper.toResponse(cartItem));
    }

    @GetMapping("/user/{id}")
    public ApiResponse<List<CartItemResponse>> getCartItemsById(@PathVariable("id") Long id) {
        List<CartItem> cartItems = cartItemUseCase.getCartItemsByUserId(id);
        return ApiResponse.apiResponseSuccess("Lấy giỏ hàng thành công", cartItemMapper.toResponses(cartItems));
    }

    @DeleteMapping("/user/{id}")
    public ApiResponse<Void> deleteCartItemsByUserId(@PathVariable("id") Long id) {
        cartItemUseCase.deleteCartItemsByUserId(id);
        return ApiResponse.apiResponseSuccess("Đã xóa toàn bộ giỏ hàng của người dùng", null);
    }
}
