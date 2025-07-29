package mx.edu.utez.sima.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import mx.edu.utez.sima.modules.article.ArticleDTO;
import mx.edu.utez.sima.services.ArticleService;
import mx.edu.utez.sima.utils.APIResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/articles")
@Tag(name = "Controlador de Artículos", description = "Operaciones relacionadas con los artículos")
public class ArticleController {

    private final ArticleService articleService;

    public ArticleController(ArticleService articleService) {
        this.articleService = articleService;
    }

    @PostMapping
    @Operation(summary = "Crear artículo", description = "Permite crear un nuevo artículo")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Artículo creado exitosamente"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<APIResponse> createArticle (@Valid @RequestBody ArticleDTO article) {
        return articleService.createArticle(article.toEntity());
    }

    @GetMapping
    @Operation(summary = "Obtener artículos", description = "Retorna todos los artículos")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de artículos obtenida exitosamente"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<APIResponse> getAllArticles() {
        return articleService.getAllArticles();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener artículo por ID", description = "Retorna el artículo según el ID proporcionado")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Artículo obtenido exitosamente"),
            @ApiResponse(responseCode = "404", description = "Artículo no encontrado"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<APIResponse> getArticleById(@PathVariable Long id) {
        return articleService.getArticleById(id);
    }

    @GetMapping("/uuid/{uuid}")
    @Operation(summary = "Obtener artículo por UUID", description = "Retorna el artículo según el UUID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Artículo obtenido exitosamente"),
            @ApiResponse(responseCode = "404", description = "Artículo no encontrado"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<APIResponse> getArticleByUuid(@PathVariable String uuid) {
        return articleService.getArticleByUuid(uuid);
    }

    @GetMapping("/search")
    @Operation(summary = "Buscar artículos", description = "Realiza búsqueda de artículos por nombre y/o categoría")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Artículos encontrados exitosamente"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<APIResponse> searchArticles(
            @RequestParam String name,
            @RequestParam(required = false) Long categoryId) {
        return articleService.searchArticles(name, categoryId);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar artículo", description = "Permite actualizar la información del artículo")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Artículo actualizado exitosamente"),
            @ApiResponse(responseCode = "404", description = "Artículo no encontrado"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<APIResponse> updateArticle(@RequestBody ArticleDTO articleDetails) {
        return articleService.updateArticle(articleDetails.toEntityUpdate());
    }

    @PutMapping("/{id}/quantity")
    @Operation(summary = "Actualizar cantidad", description = "Permite actualizar la cantidad de un artículo")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cantidad actualizada exitosamente"),
            @ApiResponse(responseCode = "404", description = "Artículo no encontrado"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<APIResponse> updateArticleQuantity(@PathVariable Long id, @RequestParam Long newQuantity) {
        return articleService.updateArticleQuantity(id, newQuantity);
    }

    @PutMapping("/{id}/increment")
    @Operation(summary = "Incrementar cantidad", description = "Incrementa la cantidad de un artículo")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cantidad incrementada exitosamente"),
            @ApiResponse(responseCode = "404", description = "Artículo no encontrado"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<APIResponse> incrementArticleQuantity(@PathVariable Long id, @RequestParam Long increment) {
        return articleService.incrementArticleQuantity(id, increment);
    }

    @PutMapping("/{id}/decrement")
    @Operation(summary = "Decrementar cantidad", description = "Decrementa la cantidad de un artículo")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cantidad decrementada exitosamente"),
            @ApiResponse(responseCode = "404", description = "Artículo no encontrado"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<APIResponse> decrementArticleQuantity(@PathVariable Long id, @RequestParam Long decrement) {
        return articleService.decrementArticleQuantity(id, decrement);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar artículo", description = "Permite eliminar un artículo por su ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Artículo eliminado exitosamente"),
            @ApiResponse(responseCode = "404", description = "Artículo no encontrado"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<APIResponse> deleteArticle(@PathVariable Long id) {
        return articleService.deleteArticle(id);
    }

    @GetMapping("/category/{categoryId}")
    @Operation(summary = "Artículos por categoría", description = "Retorna los artículos según una categoría específica")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Artículos encontrados exitosamente"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<APIResponse> getArticlesByCategory(@PathVariable Long categoryId) {
        return articleService.getArticlesByCategory(categoryId);
    }

    @GetMapping("/storage/{storageId}")
    @Operation(summary = "Artículos por almacén", description = "Retorna los artículos asociados a un almacén")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Artículos encontrados exitosamente"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<APIResponse> getArticlesByStorage(@PathVariable Long storageId) {
        return articleService.getArticlesByStorage(storageId);
    }

    @GetMapping("/low-stock")
    @Operation(summary = "Artículos con stock bajo", description = "Retorna artículos cuya cantidad es menor a un umbral")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Artículos obtenidos exitosamente"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<APIResponse> getArticlesWithLowStock(@RequestParam Long threshold) {
        return articleService.getArticlesWithLowStock(threshold);
    }

    @GetMapping("/no-stock")
    @Operation(summary = "Artículos sin stock", description = "Retorna artículos sin stock")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Artículos obtenidos exitosamente"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<APIResponse> getArticlesWithoutStock() {
        return articleService.getArticlesWithoutStock();
    }

    @GetMapping("/total/category/{categoryId}")
    @Operation(summary = "Total por categoría", description = "Retorna el total de artículos por categoría")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Total obtenido exitosamente"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<APIResponse> getTotalArticlesByCategory(@PathVariable Long categoryId) {
        return articleService.getTotalArticlesByCategory(categoryId);
    }

    @GetMapping("/total/stock")
    @Operation(summary = "Total en stock", description = "Retorna la cantidad total en stock de todos los artículos")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Total obtenido exitosamente"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<APIResponse> getTotalQuantityInStock() {
        return articleService.getTotalQuantityInStock();
    }

    @PutMapping("/storage/{storageId}/article/{articleId}")
    @Operation(
            summary = "Asignar artículo a un almacén",
            description = "Asigna una cantidad específica de un artículo a un almacén"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Artículo asignado exitosamente al almacén",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = APIResponse.class))),
            @ApiResponse(responseCode = "400", description = "Solicitud inválida (cantidad incorrecta o parámetros inválidos)",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = APIResponse.class))),
            @ApiResponse(responseCode = "404", description = "Artículo o almacén no encontrado",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = APIResponse.class))),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = APIResponse.class)))
    })
    public ResponseEntity<APIResponse> assignArticleToStorage(
            @PathVariable Long storageId,
            @PathVariable Long articleId,
            @RequestParam Long quantity) {
        return articleService.assignArticleToStorage(articleId, storageId, quantity);
    }



}