package mx.edu.utez.sima.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import mx.edu.utez.sima.services.CategoryService;
import mx.edu.utez.sima.utils.APIResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/categories")
@Tag(name = "Controlador de Categorías", description = "Operaciones relacionadas con el manejo de categorías")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @PostMapping
    @Operation(summary = "Crear categoría", description = "Permite crear una nueva categoría")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Categoría creada exitosamente"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<APIResponse> createCategory(@Valid @RequestParam String category) {
        return categoryService.createCategory(category);
    }

    @GetMapping
    @Operation(summary = "Obtener categorías", description = "Retorna todas las categorías")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de categorías obtenida exitosamente"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<APIResponse> getAllCategories() {
        return categoryService.getAllCategories();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener categoría por ID", description = "Retorna la categoría según el ID proporcionado")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Categoría obtenida exitosamente"),
            @ApiResponse(responseCode = "404", description = "Categoría no encontrada"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<APIResponse> getCategoryById(@PathVariable Long id) {
        return categoryService.getCategoryById(id);
    }

    @GetMapping("/uuid/{uuid}")
    @Operation(summary = "Obtener categoría por UUID", description = "Retorna la categoría según el UUID proporcionado")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Categoría obtenida exitosamente"),
            @ApiResponse(responseCode = "404", description = "Categoría no encontrada"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<APIResponse> getCategoryByUuid(@PathVariable String uuid) {
        return categoryService.getCategoryByUuid(uuid);
    }

    @GetMapping("/name/{name}")
    @Operation(summary = "Obtener categoría por nombre", description = "Retorna la categoría según el nombre proporcionado")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Categoría obtenida exitosamente"),
            @ApiResponse(responseCode = "404", description = "Categoría no encontrada"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<APIResponse> getCategoryByName(@PathVariable String name) {
        return categoryService.getCategoryByName(name);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar categoría", description = "Actualiza la información de una categoría existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Categoría actualizada exitosamente"),
            @ApiResponse(responseCode = "404", description = "Categoría no encontrada"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<APIResponse> updateCategory(@PathVariable Long id, @RequestParam String categoryDetails) {
        return categoryService.updateCategory(id, categoryDetails);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar categoría", description = "Elimina una categoría según su ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Categoría eliminada exitosamente"),
            @ApiResponse(responseCode = "404", description = "Categoría no encontrada"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<APIResponse> deleteCategory(@PathVariable Long id) {
        return categoryService.deleteCategory(id);
    }

    @GetMapping("/with-storages")
    @Operation(summary = "Categorías con almacenes", description = "Retorna las categorías con sus almacenes asociados")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Categorías y almacenes obtenidos exitosamente"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<APIResponse> getCategoriesWithStorages() {
        return categoryService.getCategoriesWithStorages();
    }

    @GetMapping("/with-articles")
    @Operation(summary = "Categorías con artículos", description = "Retorna las categorías con sus artículos asociados")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Categorías y artículos obtenidos exitosamente"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<APIResponse> getCategoriesWithArticles() {
        return categoryService.getCategoriesWithArticles();
    }
}