package com.greenblat.backend.todo.service;

import com.greenblat.backend.todo.entity.Category;
import com.greenblat.backend.todo.repository.CategoryRepository;
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

    public List<Category> findAll(String email) {
        return categoryRepository.findByUserEmailOrderByTitleAsc(email);
    }

    public List<Category> findByTitle(String title, String email) {
        return categoryRepository.findByTitle(email, title);
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
