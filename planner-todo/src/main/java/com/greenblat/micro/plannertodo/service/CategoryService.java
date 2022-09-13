package com.greenblat.micro.plannertodo.service;

import com.greenblat.micro.plannerentity.entity.Category;
import com.greenblat.micro.plannertodo.repository.CategoryRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public Category getById(Long id) {
        return categoryRepository.findById(id).get();
    }

    public List<Category> findAll(Long userId) {
        return categoryRepository.findByUserIdOrderByTitleAsc(userId);
    }

    public List<Category> findByTitle(String title, Long userId) {
        return categoryRepository.findByTitle(title, userId);
    }

    @Transactional
    public Category addCategory(Category category) {
        return categoryRepository.save(category);
    }

    @Transactional
    public void updateCategory(Category category) {
        categoryRepository.save(category);
    }

    @Transactional
    public void deleteCategory(Long id) {
        categoryRepository.deleteById(id);
    }
}
