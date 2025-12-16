package org.dpnam28.foodcouriers.presentation.dto.food;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class FoodResponse {
    private Long id;
    private String name;
    private String description;
    private Long categoryId;
    private String categoryName;
    private Long restaurantId;
    private Double price;
    private String image;
    private Boolean isActive;
}
