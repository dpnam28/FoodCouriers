package org.dpnam28.foodcouriers.usecase;

import lombok.RequiredArgsConstructor;
import org.dpnam28.foodcouriers.domain.entity.Category;
import org.dpnam28.foodcouriers.domain.entity.Food;
import org.dpnam28.foodcouriers.domain.exception.AppException;
import org.dpnam28.foodcouriers.domain.exception.ErrorCode;
import org.dpnam28.foodcouriers.domain.repository.CategoryRepository;
import org.dpnam28.foodcouriers.domain.repository.FoodRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryUseCase {

    private final CategoryRepository categoryRepository;
    private final FoodRepository foodRepository;

    public Category createCategory(String categoryName) {
        if (categoryRepository.findByName(categoryName) != null) {
            throw new AppException(ErrorCode.CATEGORY_ALREADY_EXISTS);
        }
        Category category = Category.builder()
                .name(categoryName)
                .build();
        return categoryRepository.save(category);
    }

    public Category updateCategory(Long id, String categoryName) {
        Category category = categoryRepository.findById(id);
        category.setName(categoryName);
        return categoryRepository.save(category);
    }

    public void deleteCategory(Long id) {
        Category category = categoryRepository.findById(id);
        categoryRepository.delete(category);
    }

    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    public List<Food> getFoodsByCategory(Long categoryId) {
        categoryRepository.findById(categoryId);
        return foodRepository.findByCategoryId(categoryId);
    }
}
