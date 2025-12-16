package org.dpnam28.foodcouriers.presentation.dto.category;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CategoryRequest {
    @NotBlank(message = "categoryName is required")
    private String categoryName;
}
