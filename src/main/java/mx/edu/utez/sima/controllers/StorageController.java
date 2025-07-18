package mx.edu.utez.sima.controllers;

import mx.edu.utez.sima.modules.storage.Storage;
import mx.edu.utez.sima.services.StorageService;
import mx.edu.utez.sima.utils.APIResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/storages")
public class StorageController {
    @Autowired
    private StorageService storageService;

    @PostMapping
    public ResponseEntity<APIResponse> createStorage(@RequestBody Storage storage) {
        return storageService.createStorage(storage);
    }

    @GetMapping
    public ResponseEntity<APIResponse> getAllStorages() {
        return storageService.getAllStorages();
    }

    @GetMapping("/{id}")
    public ResponseEntity<APIResponse> getStorageById(@PathVariable Long id) {
        return storageService.getStorageById(id);
    }

    @GetMapping("/uuid/{uuid}")
    public ResponseEntity<APIResponse> getStorageByUuid(@PathVariable String uuid) {
        return storageService.getStorageByUuid(uuid);
    }

    @GetMapping("/identifier/{identifier}")
    public ResponseEntity<APIResponse> getStorageByIdentifier(@PathVariable String identifier) {
        return storageService.getStorageByIdentifier(identifier);
    }

    @PutMapping("/{id}")
    public ResponseEntity<APIResponse> updateStorage(@PathVariable Long id, @RequestBody Storage storageDetails) {
        return storageService.updateStorage(id, storageDetails);
    }

    @PutMapping("/{storageId}/responsible/{userId}")
    public ResponseEntity<APIResponse> assignResponsible(@PathVariable Long storageId, @PathVariable Long userId) {
        return storageService.assignResponsible(storageId, userId);
    }

    @PutMapping("/{storageId}/responsible/remove")
    public ResponseEntity<APIResponse> removeResponsible(@PathVariable Long storageId) {
        return storageService.removeResponsible(storageId);
    }

    @PutMapping("/{storageId}/article/{articleId}")
    public ResponseEntity<APIResponse> addArticleToStorage(@PathVariable Long storageId, @PathVariable Long articleId) {
        return storageService.addArticleToStorage(storageId, articleId);
    }

    @DeleteMapping("/{storageId}/article/{articleId}")
    public ResponseEntity<APIResponse> removeArticleFromStorage(@PathVariable Long storageId, @PathVariable Long articleId) {
        return storageService.removeArticleFromStorage(storageId, articleId);
    }

    @PutMapping("/{id}/toggle-status")
    public ResponseEntity<APIResponse> toggleStorageStatus(@PathVariable Long id) {
        return storageService.toggleStorageStatus(id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<APIResponse> deleteStorage(@PathVariable Long id) {
        return storageService.deleteStorage(id);
    }

    @GetMapping("/category/{categoryId}")
    public ResponseEntity<APIResponse> getStoragesByCategory(@PathVariable Long categoryId) {
        return storageService.getStoragesByCategory(categoryId);
    }

    @GetMapping("/active")
    public ResponseEntity<APIResponse> getActiveStorages() {
        return storageService.getActiveStorages();
    }

    @GetMapping("/without-responsible")
    public ResponseEntity<APIResponse> getStoragesWithoutResponsible() {
        return storageService.getStoragesWithoutResponsible();
    }

    @GetMapping("/responsible/{userId}")
    public ResponseEntity<APIResponse> getStorageByResponsible(@PathVariable Long userId) {
        return storageService.getStorageByResponsible(userId);
    }
}