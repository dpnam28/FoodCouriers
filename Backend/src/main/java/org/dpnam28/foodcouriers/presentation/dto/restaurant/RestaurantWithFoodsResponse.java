package org.dpnam28.foodcouriers.presentation.dto.restaurant;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.dpnam28.foodcouriers.presentation.dto.food.FoodResponse;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RestaurantWithFoodsResponse {
    private RestaurantResponse restaurant;
    private List<FoodResponse> foods;
}
