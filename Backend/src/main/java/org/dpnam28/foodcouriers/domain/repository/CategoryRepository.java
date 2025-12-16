package org.dpnam28.foodcouriers.domain.repository;

import org.dpnam28.foodcouriers.domain.entity.Category;

import java.util.List;

public interface CategoryRepository {
    Category save(Category category);

    Category findById(Long id);

    Category findByName(String name);

    void delete(Category category);

    List<Category> findAll();
}
