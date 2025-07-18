package mx.edu.utez.sima.services;

import mx.edu.utez.sima.modules.storage.Storage;
import mx.edu.utez.sima.modules.article.Article;
import mx.edu.utez.sima.modules.article.ArticleRepository;
import mx.edu.utez.sima.modules.storage.StorageRepository;
import mx.edu.utez.sima.modules.user.BeanUser;
import mx.edu.utez.sima.modules.category.CategoryRepository;
import mx.edu.utez.sima.modules.user.UserRepository;
import mx.edu.utez.sima.utils.APIResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class StorageService {

    private static final Logger logger = LogManager.getLogger(StorageService.class);

    @Autowired
    private StorageRepository storageRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ArticleRepository articleRepository;

    // Crear almacén
    @Transactional(rollbackFor = Exception.class)
    public ResponseEntity<APIResponse> createStorage(Storage storage) {
        try {
            if (storageRepository.existsByStorageIdentifier(storage.getStorageIdentifier())) {
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body(new APIResponse("Almacén con este identificador ya existe", true, HttpStatus.CONFLICT));
            }

            storage.setUuid(UUID.randomUUID().toString());
            storage.setStatus(true);

            Storage saved = storageRepository.save(storage);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new APIResponse("Almacén creado exitosamente", saved, false, HttpStatus.CREATED));
        } catch (Exception e) {
            logger.error("Error al crear almacén: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new APIResponse("Error al crear almacén", true, HttpStatus.INTERNAL_SERVER_ERROR));
        }
    }

    // Obtener todos los almacenes
    @Transactional(readOnly = true)
    public ResponseEntity<APIResponse> getAllStorages() {
        try {
            List<Storage> storages = storageRepository.findAll();
            return ResponseEntity.ok(new APIResponse("Almacenes obtenidos", storages, false, HttpStatus.OK));
        } catch (Exception e) {
            logger.error("Error al obtener almacenes: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new APIResponse("Error al obtener almacenes", true, HttpStatus.INTERNAL_SERVER_ERROR));
        }
    }

    // Obtener almacén por ID
    @Transactional(readOnly = true)
    public ResponseEntity<APIResponse> getStorageById(Long id) {
        try {
            Optional<Storage> storage = storageRepository.findById(id);
            if (storage.isPresent()) {
                return ResponseEntity.ok(new APIResponse("Almacén encontrado", storage.get(), false, HttpStatus.OK));
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new APIResponse("Almacén no encontrado", true, HttpStatus.NOT_FOUND));
            }
        } catch (Exception e) {
            logger.error("Error al obtener almacén por ID: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new APIResponse("Error al obtener almacén", true, HttpStatus.INTERNAL_SERVER_ERROR));
        }
    }

    // Obtener almacén por UUID
    @Transactional(readOnly = true)
    public ResponseEntity<APIResponse> getStorageByUuid(String uuid) {
        try {
            Optional<Storage> storage = storageRepository.findByUuid(uuid);
            if (storage.isPresent()) {
                return ResponseEntity.ok(new APIResponse("Almacén encontrado", storage.get(), false, HttpStatus.OK));
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new APIResponse("Almacén no encontrado", true, HttpStatus.NOT_FOUND));
            }
        } catch (Exception e) {
            logger.error("Error al obtener almacén por UUID: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new APIResponse("Error al obtener almacén", true, HttpStatus.INTERNAL_SERVER_ERROR));
        }
    }

    // Obtener almacén por identificador
    @Transactional(readOnly = true)
    public ResponseEntity<APIResponse> getStorageByIdentifier(String identifier) {
        try {
            Optional<Storage> storage = storageRepository.findByStorageIdentifier(identifier);
            if (storage.isPresent()) {
                return ResponseEntity.ok(new APIResponse("Almacén encontrado", storage.get(), false, HttpStatus.OK));
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new APIResponse("Almacén no encontrado", true, HttpStatus.NOT_FOUND));
            }
        } catch (Exception e) {
            logger.error("Error al obtener almacén por identificador: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new APIResponse("Error al obtener almacén", true, HttpStatus.INTERNAL_SERVER_ERROR));
        }
    }

    // Actualizar almacén
    @Transactional(rollbackFor = Exception.class)
    public ResponseEntity<APIResponse> updateStorage(Long id, Storage storageDetails) {
        try {
            Storage storage = storageRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Almacén no encontrado"));

            if (!storage.getStorageIdentifier().equals(storageDetails.getStorageIdentifier()) &&
                    storageRepository.existsByStorageIdentifier(storageDetails.getStorageIdentifier())) {
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body(new APIResponse("Almacén con este identificador ya existe", true, HttpStatus.CONFLICT));
            }

            storage.setStorageIdentifier(storageDetails.getStorageIdentifier());
            storage.setCategory(storageDetails.getCategory());

            Storage updated = storageRepository.save(storage);
            return ResponseEntity.ok(new APIResponse("Almacén actualizado", updated, false, HttpStatus.OK));
        } catch (Exception e) {
            logger.error("Error al actualizar almacén: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new APIResponse("Error al actualizar almacén", true, HttpStatus.INTERNAL_SERVER_ERROR));
        }
    }

    // Asignar responsable a almacén
    @Transactional(rollbackFor = Exception.class)
    public ResponseEntity<APIResponse> assignResponsible(Long storageId, Long userId) {
        try {
            Storage storage = storageRepository.findById(storageId)
                    .orElseThrow(() -> new RuntimeException("Almacén no encontrado"));

            BeanUser user = userRepository.findById(userId)
                    .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

            if (user.getStorage() != null) {
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body(new APIResponse("El usuario ya tiene un almacén asignado", true, HttpStatus.CONFLICT));
            }

            if (storage.getResponsible() != null) {
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body(new APIResponse("El almacén ya tiene un responsable asignado", true, HttpStatus.CONFLICT));
            }

            storage.setResponsible(user);
            Storage updated = storageRepository.save(storage);
            return ResponseEntity.ok(new APIResponse("Responsable asignado", updated, false, HttpStatus.OK));
        } catch (Exception e) {
            logger.error("Error al asignar responsable: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new APIResponse("Error al asignar responsable", true, HttpStatus.INTERNAL_SERVER_ERROR));
        }
    }

    // Remover responsable de almacén
    @Transactional(rollbackFor = Exception.class)
    public ResponseEntity<APIResponse> removeResponsible(Long storageId) {
        try {
            Storage storage = storageRepository.findById(storageId)
                    .orElseThrow(() -> new RuntimeException("Almacén no encontrado"));

            storage.setResponsible(null);
            Storage updated = storageRepository.save(storage);
            return ResponseEntity.ok(new APIResponse("Responsable removido", updated, false, HttpStatus.OK));
        } catch (Exception e) {
            logger.error("Error al remover responsable: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new APIResponse("Error al remover responsable", true, HttpStatus.INTERNAL_SERVER_ERROR));
        }
    }

    // Agregar artículo a almacén
    @Transactional(rollbackFor = Exception.class)
    public ResponseEntity<APIResponse> addArticleToStorage(Long storageId, Long articleId) {
        try {
            Storage storage = storageRepository.findById(storageId)
                    .orElseThrow(() -> new RuntimeException("Almacén no encontrado"));

            Article article = articleRepository.findById(articleId)
                    .orElseThrow(() -> new RuntimeException("Artículo no encontrado"));

            if (!storage.getCategory().getId().equals(article.getCategory().getId())) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new APIResponse("El artículo y el almacén deben ser de la misma categoría", true, HttpStatus.BAD_REQUEST));
            }

            if (storage.getArticles().contains(article)) {
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body(new APIResponse("El artículo ya está en este almacén", true, HttpStatus.CONFLICT));
            }

            storage.getArticles().add(article);
            Storage updated = storageRepository.save(storage);
            return ResponseEntity.ok(new APIResponse("Artículo agregado al almacén", updated, false, HttpStatus.OK));
        } catch (Exception e) {
            logger.error("Error al agregar artículo: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new APIResponse("Error al agregar artículo", true, HttpStatus.INTERNAL_SERVER_ERROR));
        }
    }

    // Remover artículo de almacén
    @Transactional(rollbackFor = Exception.class)
    public ResponseEntity<APIResponse> removeArticleFromStorage(Long storageId, Long articleId) {
        try {
            Storage storage = storageRepository.findById(storageId)
                    .orElseThrow(() -> new RuntimeException("Almacén no encontrado"));

            Article article = articleRepository.findById(articleId)
                    .orElseThrow(() -> new RuntimeException("Artículo no encontrado"));

            storage.getArticles().remove(article);
            Storage updated = storageRepository.save(storage);
            return ResponseEntity.ok(new APIResponse("Artículo removido del almacén", updated, false, HttpStatus.OK));
        } catch (Exception e) {
            logger.error("Error al remover artículo: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new APIResponse("Error al remover artículo", true, HttpStatus.INTERNAL_SERVER_ERROR));
        }
    }

    // Cambiar estado de almacén
    @Transactional(rollbackFor = Exception.class)
    public ResponseEntity<APIResponse> toggleStorageStatus(Long id) {
        try {
            Storage storage = storageRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Almacén no encontrado"));

            storage.setStatus(!storage.getStatus());
            Storage updated = storageRepository.save(storage);
            return ResponseEntity.ok(new APIResponse("Estado del almacén actualizado", updated, false, HttpStatus.OK));
        } catch (Exception e) {
            logger.error("Error al cambiar estado del almacén: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new APIResponse("Error al cambiar estado", true, HttpStatus.INTERNAL_SERVER_ERROR));
        }
    }

    // Eliminar almacén
    @Transactional(rollbackFor = Exception.class)
    public ResponseEntity<APIResponse> deleteStorage(Long id) {
        try {
            Storage storage = storageRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Almacén no encontrado"));

            if (!storage.getArticles().isEmpty()) {
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body(new APIResponse("No se puede eliminar un almacén que tiene artículos", true, HttpStatus.CONFLICT));
            }

            storageRepository.delete(storage);
            return ResponseEntity.ok(new APIResponse("Almacén eliminado", false, HttpStatus.OK));
        } catch (Exception e) {
            logger.error("Error al eliminar almacén: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new APIResponse("Error al eliminar almacén", true, HttpStatus.INTERNAL_SERVER_ERROR));
        }
    }

    // Obtener almacenes por categoría
    @Transactional(readOnly = true)
    public ResponseEntity<APIResponse> getStoragesByCategory(Long categoryId) {
        try {
            List<Storage> storages = storageRepository.findByCategoryId(categoryId);
            return ResponseEntity.ok(new APIResponse("Almacenes por categoría", storages, false, HttpStatus.OK));
        } catch (Exception e) {
            logger.error("Error al obtener almacenes por categoría: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new APIResponse("Error al obtener almacenes por categoría", true, HttpStatus.INTERNAL_SERVER_ERROR));
        }
    }

    // Obtener almacenes activos
    @Transactional(readOnly = true)
    public ResponseEntity<APIResponse> getActiveStorages() {
        try {
            List<Storage> storages = storageRepository.findByStatusTrue();
            return ResponseEntity.ok(new APIResponse("Almacenes activos", storages, false, HttpStatus.OK));
        } catch (Exception e) {
            logger.error("Error al obtener almacenes activos: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new APIResponse("Error al obtener almacenes activos", true, HttpStatus.INTERNAL_SERVER_ERROR));
        }
    }

    // Obtener almacenes sin responsable
    @Transactional(readOnly = true)
    public ResponseEntity<APIResponse> getStoragesWithoutResponsible() {
        try {
            List<Storage> storages = storageRepository.findByResponsibleIsNull();
            return ResponseEntity.ok(new APIResponse("Almacenes sin responsable", storages, false, HttpStatus.OK));
        } catch (Exception e) {
            logger.error("Error al obtener almacenes sin responsable: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new APIResponse("Error al obtener almacenes sin responsable", true, HttpStatus.INTERNAL_SERVER_ERROR));
        }
    }

    // Obtener almacén por responsable
    @Transactional(readOnly = true)
    public ResponseEntity<APIResponse> getStorageByResponsible(Long userId) {
        try {
            Optional<Storage> storage = storageRepository.findByResponsibleId(userId);
            if (storage.isPresent()) {
                return ResponseEntity.ok(new APIResponse("Almacén del responsable", storage.get(), false, HttpStatus.OK));
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new APIResponse("No se encontró almacén para este responsable", true, HttpStatus.NOT_FOUND));
            }
        } catch (Exception e) {
            logger.error("Error al obtener almacén por responsable: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new APIResponse("Error al obtener almacén por responsable", true, HttpStatus.INTERNAL_SERVER_ERROR));
        }
    }
}