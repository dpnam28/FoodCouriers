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
    private final CloudinaryUseCase cloudinaryUseCase;

    public Food createFood(Food request, MultipartFile imageFile) {
        String imageUrl = cloudinaryUseCase.uploadImage(imageFile);
        request.setImage(imageUrl);
        return foodRepository.save(request);
    }

    public Food updateFood(Long id, Food request, MultipartFile imageFile) {
        Food food = getFoodById(id);
        if(food == null) {
            throw new AppException(ErrorCode.USER_NOT_FOUND);
        }
        food.setName(request.getName());
        food.setDescription(request.getDescription());
        food.setCategory(request.getCategory());
        food.setPrice(request.getPrice());
        String imageUrl = cloudinaryUseCase.uploadImage(imageFile);
        if (imageUrl != null) {
            food.setImage(imageUrl);
        }
        food.setActive(request.isActive());
        return foodRepository.save(food);
    }

    public Food getFoodById(Long id) {
        Food food = foodRepository.findById(id);
        if  (food == null) {
            throw new AppException(ErrorCode.USER_NOT_FOUND);
        }
        return food;
    }

    public List<Food> searchFoodByName(String keyword) {
        return foodRepository.searchByName(keyword);
    }
}
