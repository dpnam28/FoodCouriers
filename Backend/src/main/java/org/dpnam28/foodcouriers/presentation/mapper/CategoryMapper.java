package org.dpnam28.foodcouriers.presentation.mapper;

import org.dpnam28.foodcouriers.domain.entity.Category;
import org.dpnam28.foodcouriers.presentation.dto.category.CategoryResponse;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CategoryMapper {
    CategoryResponse toCategoryResponse(Category category);
    List<CategoryResponse> toCategoryResponseList(List<Category> categories);
}
