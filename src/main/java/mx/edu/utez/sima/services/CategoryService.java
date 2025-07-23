package mx.edu.utez.sima.services;

import mx.edu.utez.sima.modules.category.Category;
import mx.edu.utez.sima.modules.category.CategoryRepository;
import mx.edu.utez.sima.utils.APIResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
public class CategoryService {

    private static final Logger log = LoggerFactory.getLogger(CategoryService.class);

    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    // Crear categoría
    public ResponseEntity<APIResponse> createCategory(String categorysName) {
        try {
            Category foundcategory = categoryRepository.findByCategoryName(categorysName).orElse(null);

            if (foundcategory != null) {
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body(new APIResponse("Categoría con este nombre ya existe", true, HttpStatus.CONFLICT));
            }

            Category category = new Category();
            category.setCategoryName(categorysName);
            category.setUuid(UUID.randomUUID().toString());
            Category saved = categoryRepository.save(category);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new APIResponse("Categoría creada exitosamente", saved, false, HttpStatus.CREATED));

        } catch (Exception e) {
            log.error("Error al crear categoría", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new APIResponse("Error al crear categoría", true, HttpStatus.INTERNAL_SERVER_ERROR));
        }
    }

    // Obtener todas las categorías
    @Transactional(readOnly = true)
    public ResponseEntity<APIResponse> getAllCategories() {
        try {
            List<Category> categories = categoryRepository.findAll();
            return ResponseEntity.ok(new APIResponse("Categorías obtenidas", categories, false, HttpStatus.OK));
        } catch (Exception e) {
            log.error("Error al obtener categorías", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new APIResponse("Error al obtener categorías", true, HttpStatus.INTERNAL_SERVER_ERROR));
        }
    }

    // Obtener categoría por ID
    @Transactional(readOnly = true)
    public ResponseEntity<APIResponse> getCategoryById(Long id) {
        try {
            Optional<Category> category = categoryRepository.findById(id);
            if (category.isPresent()) {
                return ResponseEntity.ok(new APIResponse("Categoría encontrada", category.get(), false, HttpStatus.OK));
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new APIResponse("Categoría no encontrada", true, HttpStatus.NOT_FOUND));
            }
        } catch (Exception e) {
            log.error("Error al obtener categoría por ID", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new APIResponse("Error al obtener categoría", true, HttpStatus.INTERNAL_SERVER_ERROR));
        }
    }

    // Obtener categoría por UUID
    @Transactional(readOnly = true)
    public ResponseEntity<APIResponse> getCategoryByUuid(String uuid) {
        try {
            Optional<Category> category = categoryRepository.findByUuid(uuid);
            if (category.isPresent()) {
                return ResponseEntity.ok(new APIResponse("Categoría encontrada", category.get(), false, HttpStatus.OK));
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new APIResponse("Categoría no encontrada", true, HttpStatus.NOT_FOUND));
            }
        } catch (Exception e) {
            log.error("Error al obtener categoría por UUID", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new APIResponse("Error al obtener categoría", true, HttpStatus.INTERNAL_SERVER_ERROR));
        }
    }

    // Obtener categoría por nombre
    @Transactional(readOnly = true)
    public ResponseEntity<APIResponse> getCategoryByName(String name) {
        try {
            Optional<Category> category = categoryRepository.findByCategoryName(name);
            if (category.isPresent()) {
                return ResponseEntity.ok(new APIResponse("Categoría encontrada", category.get(), false, HttpStatus.OK));
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new APIResponse("Categoría no encontrada", true, HttpStatus.NOT_FOUND));
            }
        } catch (Exception e) {
            log.error("Error al obtener categoría por nombre", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new APIResponse("Error al obtener categoría", true, HttpStatus.INTERNAL_SERVER_ERROR));
        }
    }

    // Actualizar categoría
    public ResponseEntity<APIResponse> updateCategory(Long id, String categoryDetails) {
        try {
            Category category = categoryRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Categoría no encontrada"));

            if (!category.getCategoryName().equals(categoryDetails) &&
                    categoryRepository.existsByCategoryName(categoryDetails)) {
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body(new APIResponse("Categoría con este nombre ya existe", true, HttpStatus.CONFLICT));
            }

            category.setCategoryName(categoryDetails);
            Category updated = categoryRepository.save(category);
            return ResponseEntity.ok(new APIResponse("Categoría actualizada", updated, false, HttpStatus.OK));

        } catch (Exception e) {
            log.error("Error al actualizar categoría", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new APIResponse("Error al actualizar categoría", true, HttpStatus.INTERNAL_SERVER_ERROR));
        }
    }

    // Eliminar categoría
    public ResponseEntity<APIResponse> deleteCategory(Long id) {
        try {
            Category category = categoryRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Categoría no encontrada"));

            if (!category.getStorages().isEmpty()) {
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body(new APIResponse("No se puede eliminar una categoría que tiene almacenes asociados", true, HttpStatus.CONFLICT));
            }
            if (!category.getArticles().isEmpty()) {
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body(new APIResponse("No se puede eliminar una categoría que tiene artículos asociados", true, HttpStatus.CONFLICT));
            }

            categoryRepository.delete(category);
            return ResponseEntity.ok(new APIResponse("Categoría eliminada exitosamente",  false, HttpStatus.OK));

        } catch (Exception e) {
            log.error("Error al eliminar categoría", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new APIResponse("Error al eliminar categoría", true, HttpStatus.INTERNAL_SERVER_ERROR));
        }
    }

    // Verificar si existe categoría por nombre
    @Transactional(readOnly = true)
    public boolean existsByName(String name) {
        return categoryRepository.existsByCategoryName(name);
    }

    // Obtener categorías con almacenes
    @Transactional(readOnly = true)
    public ResponseEntity<APIResponse> getCategoriesWithStorages() {
        try {
            List<Category> categories = categoryRepository.findByStoragesIsNotEmpty();
            return ResponseEntity.ok(new APIResponse("Categorías con almacenes", categories, false, HttpStatus.OK));
        } catch (Exception e) {
            log.error("Error al obtener categorías con almacenes", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new APIResponse("Error al obtener categorías con almacenes", true, HttpStatus.INTERNAL_SERVER_ERROR));
        }
    }

    // Obtener categorías con artículos
    @Transactional(readOnly = true)
    public ResponseEntity<APIResponse> getCategoriesWithArticles() {
        try {
            List<Category> categories = categoryRepository.findByArticlesIsNotEmpty();
            return ResponseEntity.ok(new APIResponse("Categorías con artículos", categories, false, HttpStatus.OK));
        } catch (Exception e) {
            log.error("Error al obtener categorías con artículos", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new APIResponse("Error al obtener categorías con artículos", true, HttpStatus.INTERNAL_SERVER_ERROR));
        }
    }
}
