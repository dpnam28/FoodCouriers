package org.dpnam28.foodcouriers.infrastructure.repository;

import lombok.RequiredArgsConstructor;
import org.dpnam28.foodcouriers.domain.entity.Category;
import org.dpnam28.foodcouriers.domain.exception.AppException;
import org.dpnam28.foodcouriers.domain.exception.ErrorCode;
import org.dpnam28.foodcouriers.domain.repository.CategoryRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

interface JpaCategoryRepository extends JpaRepository<Category, Long> {
    Category findByNameIgnoreCase(String name);
}

@Repository
@RequiredArgsConstructor
public class CategoryRepositoryImpl implements CategoryRepository {

    private final JpaCategoryRepository jpaCategoryRepository;

    @Override
    public Category save(Category category) {
        return jpaCategoryRepository.save(category);
    }

    @Override
    public Category findById(Long id) {
        return jpaCategoryRepository.findById(id)
        .orElseThrow(() -> new AppException(ErrorCode.CATEGORY_NOT_FOUND));
    }

    @Override
    public Category findByName(String name) {
        return jpaCategoryRepository.findByNameIgnoreCase(name);
    }

    @Override
    public void delete(Category category) {
        jpaCategoryRepository.delete(category);
    }

    @Override
    public List<Category> findAll() {
        return jpaCategoryRepository.findAll();
    }
}
