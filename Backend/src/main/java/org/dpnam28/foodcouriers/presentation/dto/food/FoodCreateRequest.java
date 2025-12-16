package org.dpnam28.foodcouriers.presentation.dto.food;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class FoodCreateRequest {
    @NotBlank(message = "Tên là bắt buộc")
    private String name;
    private String description;
    @NotNull(message = "Mã nhà hàng là bắt buộc")
    private Long restaurantId;
    @NotNull(message = "Danh mục là bắt buộc")
    private Long categoryId;
    @NotNull(message = "Giá là bắt buộc")
    @DecimalMin(value = "0.0", inclusive = true, message = "Giá phải lớn hơn hoặc bằng 0")
    private Double price;
    @NotNull(message = "Trạng thái là bắt buộc")
    private Boolean isActive;
}
