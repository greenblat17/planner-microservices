package com.greenblat.micro.plannertodo.controller;

import com.greenblat.micro.plannerentity.entity.Category;
import com.greenblat.micro.plannertodo.feign.UserFeignClient;
import com.greenblat.micro.plannertodo.search.CategorySearchValues;
import com.greenblat.micro.plannertodo.service.CategoryService;
import com.greenblat.micro.plannerutils.rest.webclient.UserWebClientBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/category")
public class CategoryController {

    private final CategoryService categoryService;
    private final UserFeignClient userFeignClient;

    public CategoryController(CategoryService categoryService, UserFeignClient userFeignClient) {
        this.categoryService = categoryService;
        this.userFeignClient = userFeignClient;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Category> showCategory(@PathVariable("id") Long id) {
        Category category = categoryService.getById(id);
        return ResponseEntity.ok(category);
    }

    @GetMapping("/all")
    public List<Category> showAllCategories(@RequestParam("user_id") Long userId) {
        return categoryService.findAll(userId);
    }

    @PostMapping("/add")
    public ResponseEntity<Category> add(@RequestBody Category category) {

        if (category.getTitle() == null || category.getTitle().trim().length() == 0) {
            return new ResponseEntity("misses param: title must bu null", HttpStatus.NOT_ACCEPTABLE);
        }

        Long userId = category.getUserId();
        if (userFeignClient.findUserById(userId) != null) {
            return ResponseEntity.ok(categoryService.addCategory(category));
        }

        return new ResponseEntity("user with id=" + userId + " not found", HttpStatus.NOT_ACCEPTABLE);
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

        if (categorySearchValues.getUserId() == null || categorySearchValues.getUserId() == 0) {
            return new ResponseEntity("missed param: email", HttpStatus.NOT_ACCEPTABLE);
        }

        List<Category> categoryByTitle = categoryService.findByTitle(
                categorySearchValues.getTitle(), categorySearchValues.getUserId());

        return ResponseEntity.ok(categoryByTitle);

    }
}
