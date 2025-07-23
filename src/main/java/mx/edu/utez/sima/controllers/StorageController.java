package mx.edu.utez.sima.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import mx.edu.utez.sima.modules.storage.StorageDTO;
import mx.edu.utez.sima.services.StorageService;
import mx.edu.utez.sima.utils.APIResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/storages")
@Tag(name = "Controlador de Almacenes", description = "Operaciones relacionadas con el manejo de almacenes")
public class StorageController {

    private final StorageService storageService;

    public StorageController(StorageService storageService) {
        this.storageService = storageService;
    }

    @PostMapping
    @Operation(summary = "Crear almacén", description = "Permite crear un nuevo almacén")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Almacén creado exitosamente",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = APIResponse.class))),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = APIResponse.class)))
    })
    public ResponseEntity<APIResponse> createStorage(@RequestBody StorageDTO storage) {
        return storageService.createStorage(storage.toEntity());
    }

    @GetMapping
    @Operation(summary = "Obtener almacenes", description = "Retorna todos los almacenes")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de almacenes obtenida exitosamente",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = APIResponse.class))),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = APIResponse.class)))
    })
    public ResponseEntity<APIResponse> getAllStorages() {
        return storageService.getAllStorages();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener almacén por ID", description = "Retorna el almacén según el ID proporcionado")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Almacén obtenido exitosamente",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = APIResponse.class))),
            @ApiResponse(responseCode = "404", description = "Almacén no encontrado",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = APIResponse.class))),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = APIResponse.class)))
    })
    public ResponseEntity<APIResponse> getStorageById(@PathVariable Long id) {
        return storageService.getStorageById(id);
    }

    @GetMapping("/uuid/{uuid}")
    @Operation(summary = "Obtener almacén por UUID", description = "Retorna el almacén según el UUID proporcionado")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Almacén obtenido exitosamente",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = APIResponse.class))),
            @ApiResponse(responseCode = "404", description = "Almacén no encontrado",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = APIResponse.class))),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = APIResponse.class)))
    })
    public ResponseEntity<APIResponse> getStorageByUuid(@PathVariable String uuid) {
        return storageService.getStorageByUuid(uuid);
    }

    @GetMapping("/identifier/{identifier}")
    @Operation(summary = "Obtener almacén por identificador", description = "Retorna el almacén según el identificador proporcionado")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Almacén obtenido exitosamente",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = APIResponse.class))),
            @ApiResponse(responseCode = "404", description = "Almacén no encontrado",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = APIResponse.class))),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = APIResponse.class)))
    })
    public ResponseEntity<APIResponse> getStorageByIdentifier(@PathVariable String identifier) {
        return storageService.getStorageByIdentifier(identifier);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar almacén", description = "Actualiza la información de un almacén existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Almacén actualizado exitosamente",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = APIResponse.class))),
            @ApiResponse(responseCode = "404", description = "Almacén no encontrado",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = APIResponse.class))),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = APIResponse.class)))
    })
    public ResponseEntity<APIResponse> updateStorage(@RequestBody StorageDTO storageDetails) {
        return storageService.updateStorage(storageDetails.toEntityUpdate());
    }

    @PutMapping("/{storageId}/responsible/{userId}")
    @Operation(summary = "Asignar responsable", description = "Asigna un usuario responsable a un almacén")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Responsable asignado exitosamente",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = APIResponse.class))),
            @ApiResponse(responseCode = "404", description = "Almacén o usuario no encontrado",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = APIResponse.class))),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = APIResponse.class)))
    })
    public ResponseEntity<APIResponse> assignResponsible(@PathVariable Long storageId, @PathVariable Long userId) {
        return storageService.assignResponsible(storageId, userId);
    }



    @PutMapping("/{id}/toggle-status")
    @Operation(summary = "Cambiar estado del almacén", description = "Alterna el estado activo/inactivo de un almacén")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Estado del almacén actualizado exitosamente",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = APIResponse.class))),
            @ApiResponse(responseCode = "404", description = "Almacén no encontrado",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = APIResponse.class))),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = APIResponse.class)))
    })
    public ResponseEntity<APIResponse> toggleStorageStatus(@PathVariable Long id) {
        return storageService.toggleStorageStatus(id);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar almacén", description = "Elimina un almacén según el ID proporcionado")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Almacén eliminado exitosamente",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = APIResponse.class))),
            @ApiResponse(responseCode = "404", description = "Almacén no encontrado",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = APIResponse.class))),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = APIResponse.class)))
    })
    public ResponseEntity<APIResponse> deleteStorage(@PathVariable Long id) {
        return storageService.deleteStorage(id);
    }

    @GetMapping("/category/{categoryId}")
    @Operation(summary = "Obtener almacenes por categoría", description = "Retorna los almacenes asociados a una categoría específica")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Almacenes obtenidos exitosamente",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = APIResponse.class))),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = APIResponse.class)))
    })
    public ResponseEntity<APIResponse> getStoragesByCategory(@PathVariable Long categoryId) {
        return storageService.getStoragesByCategory(categoryId);
    }

    @GetMapping("/active")
    @Operation(summary = "Obtener almacenes activos", description = "Retorna los almacenes que se encuentran activos")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Almacenes activos obtenidos exitosamente",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = APIResponse.class))),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = APIResponse.class)))
    })
    public ResponseEntity<APIResponse> getActiveStorages() {
        return storageService.getActiveStorages();
    }

    @GetMapping("/without-responsible")
    @Operation(summary = "Obtener almacenes sin responsable", description = "Retorna los almacenes que no tienen asignado un responsable")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Almacenes obtenidos exitosamente",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = APIResponse.class))),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = APIResponse.class)))
    })
    public ResponseEntity<APIResponse> getStoragesWithoutResponsible() {
        return storageService.getStoragesWithoutResponsible();
    }

    @GetMapping("/responsible/{userId}")
    @Operation(summary = "Obtener almacén por responsable", description = "Retorna el almacén asignado a un usuario responsable")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Almacén obtenido exitosamente",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = APIResponse.class))),
            @ApiResponse(responseCode = "404", description = "Almacén no encontrado",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = APIResponse.class))),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = APIResponse.class)))
    })
    public ResponseEntity<APIResponse> getStorageByResponsible(@PathVariable Long userId) {
        return storageService.getStorageByResponsible(userId);
    }


    @DeleteMapping("/storage/{storageId}/article/{articleId}")
    @Operation(summary = "Remover artículo de un almacén", description = "Elimina una cantidad específica de un artículo de un almacén y lo retorna a la bodega principal")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Artículo removido exitosamente del almacén",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = APIResponse.class))),
            @ApiResponse(responseCode = "400", description = "Solicitud inválida (parámetros incorrectos o cantidad insuficiente)",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = APIResponse.class))),
            @ApiResponse(responseCode = "404", description = "Artículo o almacén no encontrado",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = APIResponse.class))),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = APIResponse.class)))
    })
    public ResponseEntity<APIResponse> removeArticleFromStorage(
            @PathVariable Long storageId,
            @PathVariable Long articleId,
            @RequestParam Long quantity) {
        return storageService.removeArticleToStorage(storageId, articleId, quantity);
    }

}