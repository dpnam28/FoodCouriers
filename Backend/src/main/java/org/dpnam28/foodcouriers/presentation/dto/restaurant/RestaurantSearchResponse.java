package org.dpnam28.foodcouriers.presentation.dto.restaurant;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class RestaurantSearchResponse {
    private Long id;
    private String name;
    private String address;
    private String location;
    private String description;
    private String bannerImage;
}
