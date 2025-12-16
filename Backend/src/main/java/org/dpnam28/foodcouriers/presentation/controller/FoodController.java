package org.dpnam28.foodcouriers.presentation.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dpnam28.foodcouriers.domain.dto.ApiResponse;
import org.dpnam28.foodcouriers.domain.entity.Food;
import org.dpnam28.foodcouriers.presentation.dto.food.FoodCreateRequest;
import org.dpnam28.foodcouriers.presentation.dto.food.FoodResponse;
import org.dpnam28.foodcouriers.presentation.dto.food.FoodUpdateRequest;
import org.dpnam28.foodcouriers.presentation.mapper.FoodMapper;
import org.dpnam28.foodcouriers.usecase.FoodUseCase;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/foods")
@RequiredArgsConstructor
public class FoodController {

    private final FoodUseCase foodUseCase;
    private final FoodMapper foodMapper;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ApiResponse<FoodResponse> createFood(@Valid @ModelAttribute FoodCreateRequest request,
                                                @RequestPart(value = "image", required = false) MultipartFile image) {
        Food food = foodUseCase.createFood(request, image);
        return ApiResponse.apiResponseSuccess("Create food succeeded", foodMapper.toFoodResponse(food));
    }

    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ApiResponse<FoodResponse> updateFood(@PathVariable Long id,
                                                @Valid @ModelAttribute FoodUpdateRequest request,
                                                @RequestPart(value = "image", required = false) MultipartFile image) {
        Food food = foodUseCase.updateFood(id, request, image);
        return ApiResponse.apiResponseSuccess("Update food succeeded", foodMapper.toFoodResponse(food));
    }

    @GetMapping("/{id}")
    public ApiResponse<FoodResponse> getFoodById(@PathVariable Long id) {
        Food food = foodUseCase.getFoodById(id);
        return ApiResponse.apiResponseSuccess("Get food succeeded", foodMapper.toFoodResponse(food));
    }

    @GetMapping("/search")
    public ApiResponse<List<FoodResponse>> searchFoods(@RequestParam(required = false) String keyword) {
        List<Food> foods = foodUseCase.searchFoodByName(keyword);
        return ApiResponse.apiResponseSuccess("Search foods succeeded", foodMapper.toFoodResponses(foods));
    }
}
