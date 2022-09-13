package com.greenblat.backend.todo.controller;

import com.greenblat.backend.todo.entity.Category;
import com.greenblat.backend.todo.search.CategorySearchValues;
import com.greenblat.backend.todo.service.CategoryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/category")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Category> findById(@PathVariable("id") Long id) {
        Category category = categoryService.getById(id);
        return ResponseEntity.ok(category);
    }

    @GetMapping("/all")
    public List<Category> findAll(@RequestParam("email") String email) {
        return categoryService.findAll(email);
    }

    @PostMapping("/add")
    public ResponseEntity<Category> add(@RequestBody Category category) {

        if (category.getTitle() == null || category.getTitle().trim().length() == 0) {
            return new ResponseEntity("misses param: title must bu null", HttpStatus.NOT_ACCEPTABLE);
        }

        return ResponseEntity.ok(categoryService.addCategory(category));
    }

    @PutMapping("/update")
    public ResponseEntity<Category> update(@RequestBody Category category) {
        if (category.getId() == null || category.getId() == 0) {
            return new ResponseEntity("missed param: id", HttpStatus.NOT_ACCEPTABLE);
        }

        if (category.getTitle() == null || category.getTitle().trim().length() == 0) {
            return new ResponseEntity("missed param: title", HttpStatus.NOT_ACCEPTABLE);
        }

        categoryService.updateCategory(category);
        return new ResponseEntity(HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Category> delete(@PathVariable("id") Long id) {
        categoryService.deleteCategory(id);
        return new ResponseEntity(HttpStatus.OK);
    }

    @PostMapping("/search")
    public ResponseEntity<List<Category>> search(@RequestBody CategorySearchValues categorySearchValues) {

        if (categorySearchValues.getEmail() == null || categorySearchValues.getEmail().trim().length() == 0) {
            return new ResponseEntity("missed param: email", HttpStatus.NOT_ACCEPTABLE);
        }

        List<Category> categoryByTitle = categoryService.findByTitle(
                categorySearchValues.getTitle(), categorySearchValues.getEmail());

        return  ResponseEntity.ok(categoryByTitle);

    }
}
