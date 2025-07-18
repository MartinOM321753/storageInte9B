package mx.edu.utez.sima.controllers;

import mx.edu.utez.sima.modules.article.Article;
import mx.edu.utez.sima.services.ArticleService;
import mx.edu.utez.sima.utils.APIResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/articles")
public class ArticleController {
    @Autowired
    private ArticleService articleService;

    @PostMapping
    public ResponseEntity<APIResponse> createArticle(@RequestBody Article article) {
        return articleService.createArticle(article);
    }

    @GetMapping
    public ResponseEntity<APIResponse> getAllArticles() {
        return articleService.getAllArticles();
    }

    @GetMapping("/{id}")
    public ResponseEntity<APIResponse> getArticleById(@PathVariable Long id) {
        return articleService.getArticleById(id);
    }

    @GetMapping("/uuid/{uuid}")
    public ResponseEntity<APIResponse> getArticleByUuid(@PathVariable String uuid) {
        return articleService.getArticleByUuid(uuid);
    }

    @GetMapping("/search")
    public ResponseEntity<APIResponse> searchArticles(
            @RequestParam String name,
            @RequestParam(required = false) Long categoryId) {
        return articleService.searchArticles(name, categoryId);
    }

    @PutMapping("/{id}")
    public ResponseEntity<APIResponse> updateArticle(@PathVariable Long id, @RequestBody Article articleDetails) {
        return articleService.updateArticle(id, articleDetails);
    }

    @PutMapping("/{id}/quantity")
    public ResponseEntity<APIResponse> updateArticleQuantity(@PathVariable Long id, @RequestParam Long newQuantity) {
        return articleService.updateArticleQuantity(id, newQuantity);
    }

    @PutMapping("/{id}/increment")
    public ResponseEntity<APIResponse> incrementArticleQuantity(@PathVariable Long id, @RequestParam Long increment) {
        return articleService.incrementArticleQuantity(id, increment);
    }

    @PutMapping("/{id}/decrement")
    public ResponseEntity<APIResponse> decrementArticleQuantity(@PathVariable Long id, @RequestParam Long decrement) {
        return articleService.decrementArticleQuantity(id, decrement);
    }

    @PutMapping("/{id}/category/{categoryId}")
    public ResponseEntity<APIResponse> assignCategory(@PathVariable Long id, @PathVariable Long categoryId) {
        return articleService.assignCategory(id, categoryId);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<APIResponse> deleteArticle(@PathVariable Long id) {
        return articleService.deleteArticle(id);
    }

    @GetMapping("/category/{categoryId}")
    public ResponseEntity<APIResponse> getArticlesByCategory(@PathVariable Long categoryId) {
        return articleService.getArticlesByCategory(categoryId);
    }

    @GetMapping("/storage/{storageId}")
    public ResponseEntity<APIResponse> getArticlesByStorage(@PathVariable Long storageId) {
        return articleService.getArticlesByStorage(storageId);
    }

    @GetMapping("/low-stock")
    public ResponseEntity<APIResponse> getArticlesWithLowStock(@RequestParam Long threshold) {
        return articleService.getArticlesWithLowStock(threshold);
    }

    @GetMapping("/no-stock")
    public ResponseEntity<APIResponse> getArticlesWithoutStock() {
        return articleService.getArticlesWithoutStock();
    }

    @GetMapping("/can-add")
    public ResponseEntity<APIResponse> canAddToStorage(
            @RequestParam Long articleId,
            @RequestParam Long storageId) {
        return articleService.canAddToStorage(articleId, storageId);
    }

    @GetMapping("/total/category/{categoryId}")
    public ResponseEntity<APIResponse> getTotalArticlesByCategory(@PathVariable Long categoryId) {
        return articleService.getTotalArticlesByCategory(categoryId);
    }

    @GetMapping("/total/stock")
    public ResponseEntity<APIResponse> getTotalQuantityInStock() {
        return articleService.getTotalQuantityInStock();
    }
}