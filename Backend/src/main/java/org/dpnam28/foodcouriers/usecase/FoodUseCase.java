package org.dpnam28.foodcouriers.usecase;

import lombok.RequiredArgsConstructor;
import org.dpnam28.foodcouriers.domain.entity.Category;
import org.dpnam28.foodcouriers.domain.entity.Food;
import org.dpnam28.foodcouriers.domain.entity.Restaurant;
import org.dpnam28.foodcouriers.domain.exception.AppException;
import org.dpnam28.foodcouriers.domain.exception.ErrorCode;
import org.dpnam28.foodcouriers.domain.repository.CategoryRepository;
import org.dpnam28.foodcouriers.domain.repository.FoodRepository;
import org.dpnam28.foodcouriers.domain.repository.RestaurantRepository;
import org.dpnam28.foodcouriers.presentation.dto.food.FoodCreateRequest;
import org.dpnam28.foodcouriers.presentation.dto.food.FoodUpdateRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FoodUseCase {

    private final FoodRepository foodRepository;
    private final CategoryRepository categoryRepository;
    private final RestaurantRepository restaurantRepository;
    private final CloudinaryUseCase cloudinaryUseCase;

    public Food createFood(FoodCreateRequest request, MultipartFile imageFile) {
        Category category = getCategoryOrThrow(request.getCategoryId());
        Restaurant restaurant = getRestaurantOrThrow(request.getRestaurantId());
        String imageUrl = cloudinaryUseCase.uploadImage(imageFile);
        Food food = Food.builder()
                .name(request.getName())
                .description(request.getDescription())
                .category(category)
                .restaurant(restaurant)
                .price(request.getPrice())
                .image(imageUrl)
                .isActive(request.getIsActive())
                .build();
        return foodRepository.save(food);
    }

    public Food updateFood(Long id, FoodUpdateRequest request, MultipartFile imageFile) {
        Food food = getFoodOrThrow(id);
        Category category = getCategoryOrThrow(request.getCategoryId());
        food.setName(request.getName());
        food.setDescription(request.getDescription());
        food.setCategory(category);
        food.setPrice(request.getPrice());
        String imageUrl = cloudinaryUseCase.uploadImage(imageFile);
        if (imageUrl != null) {
            food.setImage(imageUrl);
        }
        food.setActive(request.getIsActive());
        return foodRepository.save(food);
    }

    public Food getFoodById(Long id) {
        return getFoodOrThrow(id);
    }

    public List<Food> searchFoodByName(String keyword) {
        return foodRepository.searchByName(keyword);
    }

    private Food getFoodOrThrow(Long id) {
        Food food = foodRepository.findById(id);
        if (food == null) {
            throw new AppException(ErrorCode.FOOD_NOT_FOUND);
        }
        return food;
    }

    private Category getCategoryOrThrow(Long id) {
        Category category = categoryRepository.findById(id);
        if (category == null) {
            throw new AppException(ErrorCode.CATEGORY_NOT_FOUND);
        }
        return category;
    }

    private Restaurant getRestaurantOrThrow(Long id) {
        Restaurant restaurant = restaurantRepository.findById(id);
        if (restaurant == null) {
            throw new AppException(ErrorCode.RESTAURANT_NOT_FOUND);
        }
        return restaurant;
    }
}
