package org.dpnam28.foodcouriers.presentation.mapper;

import org.dpnam28.foodcouriers.domain.entity.CartItem;
import org.dpnam28.foodcouriers.presentation.dto.cart.CartItemResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CartItemMapper {

    @Mapping(target = "userId", source = "user.id")
    @Mapping(target = "userName", source = "user.fullName")
    @Mapping(target = "foodId", source = "food.id")
    @Mapping(target = "foodName", source = "food.name")
    CartItemResponse toResponse(CartItem cartItem);

    List<CartItemResponse> toResponses(List<CartItem> cartItems);
}
