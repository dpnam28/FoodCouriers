package org.dpnam28.foodcouriers.presentation.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dpnam28.foodcouriers.domain.dto.ApiResponse;
import org.dpnam28.foodcouriers.domain.entity.Category;
import org.dpnam28.foodcouriers.domain.entity.Food;
import org.dpnam28.foodcouriers.presentation.dto.category.CategoryRequest;
import org.dpnam28.foodcouriers.presentation.dto.category.CategoryResponse;
import org.dpnam28.foodcouriers.presentation.dto.food.FoodResponse;
import org.dpnam28.foodcouriers.presentation.mapper.CategoryMapper;
import org.dpnam28.foodcouriers.presentation.mapper.FoodMapper;
import org.dpnam28.foodcouriers.usecase.CategoryUseCase;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryUseCase categoryUseCase;
    private final CategoryMapper categoryMapper;
    private final FoodMapper foodMapper;

    @PostMapping
    public ApiResponse<CategoryResponse> createCategory(@RequestBody @Valid CategoryRequest request) {
        Category category = categoryUseCase.createCategory(request.getCategoryName());
        return ApiResponse.apiResponseSuccess("Create category succeeded", categoryMapper.toCategoryResponse(category));
    }

    @PutMapping("/{id}")
    public ApiResponse<CategoryResponse> updateCategory(@PathVariable Long id,
                                                        @RequestBody @Valid CategoryRequest request) {
        Category category = categoryUseCase.updateCategory(id, request.getCategoryName());
        return ApiResponse.apiResponseSuccess("Update category succeeded", categoryMapper.toCategoryResponse(category));
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Object> deleteCategory(@PathVariable Long id) {
        categoryUseCase.deleteCategory(id);
        return ApiResponse.apiResponseSuccess("Delete category succeeded", null);
    }

    @GetMapping
    public ApiResponse<List<CategoryResponse>> getAllCategories() {
        List<Category> categories = categoryUseCase.getAllCategories();
        return ApiResponse.apiResponseSuccess("Get categories succeeded", categoryMapper.toCategoryResponseList(categories));
    }

    @GetMapping("/{id}/foods")
    public ApiResponse<List<FoodResponse>> getFoodsByCategory(@PathVariable Long id) {
        List<Food> foods = categoryUseCase.getFoodsByCategory(id);
        return ApiResponse.apiResponseSuccess("Get foods by category succeeded", foodMapper.toFoodResponses(foods));
    }
}
