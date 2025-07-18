package mx.edu.utez.sima.controllers;

import mx.edu.utez.sima.modules.category.Category;
import mx.edu.utez.sima.services.CategoryService;
import mx.edu.utez.sima.utils.APIResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    @PostMapping
    public ResponseEntity<APIResponse> createCategory(@RequestBody Category category) {
        return categoryService.createCategory(category);
    }

    @GetMapping
    public ResponseEntity<APIResponse> getAllCategories() {
        return categoryService.getAllCategories();
    }

    @GetMapping("/{id}")
    public ResponseEntity<APIResponse> getCategoryById(@PathVariable Long id) {
        return categoryService.getCategoryById(id);
    }

    @GetMapping("/uuid/{uuid}")
    public ResponseEntity<APIResponse> getCategoryByUuid(@PathVariable String uuid) {
        return categoryService.getCategoryByUuid(uuid);
    }

    @GetMapping("/name/{name}")
    public ResponseEntity<APIResponse> getCategoryByName(@PathVariable String name) {
        return categoryService.getCategoryByName(name);
    }

    @PutMapping("/{id}")
    public ResponseEntity<APIResponse> updateCategory(@PathVariable Long id, @RequestBody Category categoryDetails) {
        return categoryService.updateCategory(id, categoryDetails);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<APIResponse> deleteCategory(@PathVariable Long id) {
        return categoryService.deleteCategory(id);
    }

    @GetMapping("/with-storages")
    public ResponseEntity<APIResponse> getCategoriesWithStorages() {
        return categoryService.getCategoriesWithStorages();
    }

    @GetMapping("/with-articles")
    public ResponseEntity<APIResponse> getCategoriesWithArticles() {
        return categoryService.getCategoriesWithArticles();
    }
}