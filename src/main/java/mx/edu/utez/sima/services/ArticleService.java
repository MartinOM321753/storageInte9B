package mx.edu.utez.sima.services;

import mx.edu.utez.sima.modules.article.Article;
import mx.edu.utez.sima.modules.article.ArticleRepository;
import mx.edu.utez.sima.modules.category.Category;
import mx.edu.utez.sima.modules.storage.Storage;
import mx.edu.utez.sima.modules.category.CategoryRepository;
import mx.edu.utez.sima.modules.storage.StorageRepository;
import mx.edu.utez.sima.modules.storageHasArticle.StorageHasArticle;
import mx.edu.utez.sima.modules.storageHasArticle.StorageHasArticleRepository;
import mx.edu.utez.sima.utils.APIResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ArticleService {

    private final ArticleRepository articleRepository;

    private final CategoryRepository categoryRepository;

    private final StorageRepository storageRepository;

    private final StorageHasArticleRepository storageHasArticleRepository;

    public ArticleService(ArticleRepository articleRepository, CategoryRepository categoryRepository, StorageRepository storageRepository, StorageHasArticleRepository storageHasArticleRepository) {
        this.articleRepository = articleRepository;
        this.categoryRepository = categoryRepository;
        this.storageRepository = storageRepository;
        this.storageHasArticleRepository = storageHasArticleRepository;
    }


    @Transactional(rollbackFor = Exception.class)
    public ResponseEntity<APIResponse> createArticle(Article article) {
        try {

            Optional<Article> foundArticle=  articleRepository.findByArticleName(article.getArticleName());
            Optional<Category> category =  categoryRepository.findById(article.getCategory().getId());
            if (foundArticle.isPresent()) {
                return ResponseEntity.badRequest().body(new APIResponse("El artículo ya existe", true, HttpStatus.BAD_REQUEST));
            }

            if (category.isEmpty()) {
                return ResponseEntity.badRequest().body(new APIResponse("Categoría no encontrada", true, HttpStatus.BAD_REQUEST));
            }

            article.setUuid(UUID.randomUUID().toString());

            if (article.getQuantity() <= 0) {
                return ResponseEntity.badRequest().body(new APIResponse("La cantidad debe ser mayor a 0", true, HttpStatus.BAD_REQUEST));
            }

            article.setCategory(category.get());
            Article saved = articleRepository.save(article);
            return ResponseEntity.ok(new APIResponse("Artículo creado correctamente", saved, false, HttpStatus.OK));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    new APIResponse("Error al crear el artículo", true, HttpStatus.INTERNAL_SERVER_ERROR));
        }
    }

    @Transactional(readOnly = true)
    public ResponseEntity<APIResponse> getAllArticles() {
        try {
            List<Article> articles = articleRepository.findAll();
            return ResponseEntity.ok(new APIResponse("Lista obtenida", articles, false, HttpStatus.OK));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new APIResponse("Error al obtener los artículos", true, HttpStatus.INTERNAL_SERVER_ERROR));
        }
    }

    @Transactional(readOnly = true)
    public ResponseEntity<APIResponse> getArticleById(Long id) {
        try {
            return articleRepository.findById(id)
                    .map(article -> ResponseEntity.ok(new APIResponse("Artículo encontrado", article, false, HttpStatus.OK)))
                    .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND)
                            .body(new APIResponse("Artículo no encontrado", true, HttpStatus.NOT_FOUND)));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new APIResponse("Error al buscar artículo", true, HttpStatus.INTERNAL_SERVER_ERROR));
        }
    }

    @Transactional(readOnly = true)
    public ResponseEntity<APIResponse> getArticleByUuid(String uuid) {
        try {
            return articleRepository.findByUuid(uuid)
                    .map(article -> ResponseEntity.ok(new APIResponse("Artículo encontrado", article, false, HttpStatus.OK)))
                    .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND)
                            .body(new APIResponse("Artículo no encontrado", true, HttpStatus.NOT_FOUND)));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new APIResponse("Error al buscar artículo", true, HttpStatus.INTERNAL_SERVER_ERROR));
        }
    }

    @Transactional(readOnly = true)
    public ResponseEntity<APIResponse> getArticlesByName(String name) {
        try {
            List<Article> articles = articleRepository.findByArticleNameContainingIgnoreCase(name);
            return ResponseEntity.ok(new APIResponse("Artículos encontrados", articles, false, HttpStatus.OK));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new APIResponse("Error al buscar artículos", true, HttpStatus.INTERNAL_SERVER_ERROR));
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public ResponseEntity<APIResponse> updateArticle(Article articleDetails) {
        try {

            Article article = articleRepository.findById(articleDetails.getId()).orElse(null);

            Optional<Category> category =  categoryRepository.findById(articleDetails.getCategory().getId());

            if (article == null) {
                return ResponseEntity.badRequest().body(new APIResponse("El artículo no existe", true, HttpStatus.BAD_REQUEST));
            }

            if (category.isEmpty()) {
                return ResponseEntity.badRequest().body(new APIResponse("Categoría no encontrada", true, HttpStatus.BAD_REQUEST));
            }

            if (!article.getArticleName().equals(articleDetails.getArticleName())){
                Optional<Article> foundArticle = articleRepository.findByArticleName(articleDetails.getArticleName());
                if (foundArticle.isPresent()) {
                    return ResponseEntity.badRequest().body(new APIResponse("El nombre del artículo ya existe", true, HttpStatus.BAD_REQUEST));
                }
            }

            article.setArticleName(articleDetails.getArticleName());
            article.setDescription(articleDetails.getDescription());
            article.setQuantity(articleDetails.getQuantity());
            article.setCategory(category.get());

            if (article.getQuantity() <= 0) {
                return ResponseEntity.badRequest().body(new APIResponse("La cantidad debe ser mayor a 0", true, HttpStatus.BAD_REQUEST));
            }

            Article updated = articleRepository.save(article);
            return ResponseEntity.ok(new APIResponse("Artículo actualizado", updated, false, HttpStatus.OK));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new APIResponse("Error al actualizar el artículo", true, HttpStatus.INTERNAL_SERVER_ERROR));
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public ResponseEntity<APIResponse> updateArticleQuantity(Long id, Long newQuantity) {
        try {
            Article article = articleRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Artículo no encontrado"));

            if (newQuantity < 0) {
                return ResponseEntity.badRequest().body(new APIResponse("La cantidad no puede ser negativa", true, HttpStatus.BAD_REQUEST));
            }

            article.setQuantity(newQuantity);
            return ResponseEntity.ok(new APIResponse("Cantidad actualizada", articleRepository.save(article), false, HttpStatus.OK));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new APIResponse("Error al actualizar la cantidad", true, HttpStatus.INTERNAL_SERVER_ERROR));
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public ResponseEntity<APIResponse> incrementArticleQuantity(Long id, Long increment) {
        try {
            Article article = articleRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Artículo no encontrado"));

            if (increment <= 0) {
                return ResponseEntity.badRequest().body(new APIResponse("El incremento debe ser mayor a 0", true, HttpStatus.BAD_REQUEST));
            }

            article.setQuantity(article.getQuantity() + increment);
            return ResponseEntity.ok(new APIResponse("Cantidad incrementada", articleRepository.save(article), false, HttpStatus.OK));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new APIResponse("Error al incrementar la cantidad", true, HttpStatus.INTERNAL_SERVER_ERROR));
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public ResponseEntity<APIResponse> decrementArticleQuantity(Long id, Long decrement) {
        try {
            Article article = articleRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Artículo no encontrado"));

            if (decrement <= 0) {
                return ResponseEntity.badRequest().body(new APIResponse("El decremento debe ser mayor a 0", true, HttpStatus.BAD_REQUEST));
            }

            if (article.getQuantity() < decrement) {
                return ResponseEntity.badRequest().body(new APIResponse("No hay suficiente cantidad disponible", true, HttpStatus.BAD_REQUEST));
            }

            article.setQuantity(article.getQuantity() - decrement);
            return ResponseEntity.ok(new APIResponse("Cantidad decrementada", articleRepository.save(article), false, HttpStatus.OK));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new APIResponse("Error al decrementar la cantidad", true, HttpStatus.INTERNAL_SERVER_ERROR));
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public ResponseEntity<APIResponse> assignCategory(Long articleId, Long categoryId) {
        try {
            Article article = articleRepository.findById(articleId)
                    .orElseThrow(() -> new RuntimeException("Artículo no encontrado"));

            article.setCategory(categoryRepository.findById(categoryId)
                    .orElseThrow(() -> new RuntimeException("Categoría no encontrada")));

            return ResponseEntity.ok(new APIResponse("Categoría asignada", articleRepository.save(article), false, HttpStatus.OK));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new APIResponse("Error al asignar categoría", true, HttpStatus.INTERNAL_SERVER_ERROR));
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public ResponseEntity<APIResponse> deleteArticle(Long id) {
        try {
            Article article = articleRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Artículo no encontrado"));

            if (!article.getStorages().isEmpty()) {
                return ResponseEntity.badRequest().body(new APIResponse("No se puede eliminar un artículo que está en almacenes", true, HttpStatus.BAD_REQUEST));
            }

            articleRepository.delete(article);
            return ResponseEntity.ok(new APIResponse("Artículo eliminado",  false, HttpStatus.OK));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new APIResponse("Error al eliminar el artículo", true, HttpStatus.INTERNAL_SERVER_ERROR));
        }
    }

    @Transactional(readOnly = true)
    public ResponseEntity<APIResponse> getArticlesByCategory(Long categoryId) {
        try {
            List<Article> articles = articleRepository.findByCategoryId(categoryId);
            return ResponseEntity.ok(new APIResponse("Artículos por categoría", articles, false, HttpStatus.OK));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new APIResponse("Error al obtener artículos por categoría", true, HttpStatus.INTERNAL_SERVER_ERROR));
        }
    }

    @Transactional(readOnly = true)
    public ResponseEntity<APIResponse> getArticlesByStorage(Long storageId) {
        try {
            List<Article> articles = articleRepository.findByStoragesId(storageId);
            return ResponseEntity.ok(new APIResponse("Artículos por almacén", articles, false, HttpStatus.OK));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new APIResponse("Error al obtener artículos por almacén", true, HttpStatus.INTERNAL_SERVER_ERROR));
        }
    }

    @Transactional(readOnly = true)
    public ResponseEntity<APIResponse> getArticlesWithLowStock(Long threshold) {
        try {
            List<Article> articles = articleRepository.findByQuantityLessThan(threshold);
            return ResponseEntity.ok(new APIResponse("Artículos con stock bajo", articles, false, HttpStatus.OK));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new APIResponse("Error al obtener artículos con bajo stock", true, HttpStatus.INTERNAL_SERVER_ERROR));
        }
    }

    @Transactional(readOnly = true)
    public ResponseEntity<APIResponse> getArticlesWithoutStock() {
        try {
            List<Article> articles = articleRepository.findByQuantity(0L);
            return ResponseEntity.ok(new APIResponse("Artículos sin stock", articles, false, HttpStatus.OK));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new APIResponse("Error al obtener artículos sin stock", true, HttpStatus.INTERNAL_SERVER_ERROR));
        }
    }

    @Transactional(readOnly = true)
    public ResponseEntity<APIResponse> canAddToStorage(Long articleId, Long storageId) {
        try {
            Article article = articleRepository.findById(articleId)
                    .orElseThrow(() -> new RuntimeException("Artículo no encontrado"));

            Storage storage = storageRepository.findById(storageId)
                    .orElseThrow(() -> new RuntimeException("Almacén no encontrado"));


            boolean isValid = article.getCategory().getId().equals(storage.getCategory().getId());
            return ResponseEntity.ok(new APIResponse("Validación completada", isValid, false, HttpStatus.OK));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new APIResponse("Error al validar compatibilidad", true, HttpStatus.INTERNAL_SERVER_ERROR));
        }
    }

    @Transactional(readOnly = true)
    public ResponseEntity<APIResponse> searchArticles(String name, Long categoryId) {
        try {
            List<Article> articles = (categoryId != null)
                    ? articleRepository.findByArticleNameContainingIgnoreCaseAndCategoryId(name, categoryId)
                    : articleRepository.findByArticleNameContainingIgnoreCase(name);
            return ResponseEntity.ok(new APIResponse("Búsqueda completada", articles, false, HttpStatus.OK));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new APIResponse("Error al buscar artículos", true, HttpStatus.INTERNAL_SERVER_ERROR));
        }
    }

    @Transactional(readOnly = true)
    public ResponseEntity<APIResponse> getTotalArticlesByCategory(Long categoryId) {
        try {
            Long total = articleRepository.countByCategoryId(categoryId);
            return ResponseEntity.ok(new APIResponse("Total por categoría", total, false, HttpStatus.OK));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new APIResponse("Error al contar artículos por categoría", true, HttpStatus.INTERNAL_SERVER_ERROR));
        }
    }

    @Transactional(readOnly = true)
    public ResponseEntity<APIResponse> getTotalQuantityInStock() {
        try {
            Long total = articleRepository.sumAllQuantities();
            return ResponseEntity.ok(new APIResponse("Total en inventario", total, false, HttpStatus.OK));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new APIResponse("Error al calcular total en inventario", true, HttpStatus.INTERNAL_SERVER_ERROR));
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public ResponseEntity<APIResponse> assignArticleToStorage(Long articleId, Long storageId, Long assignQuantity) {
        try {
            Article article = articleRepository.findById(articleId)
                    .orElseThrow(() -> new RuntimeException("Artículo no encontrado"));
            Storage storage = storageRepository.findById(storageId)
                    .orElseThrow(() -> new RuntimeException("Almacén no encontrado"));

            // Validación: que el artículo y el almacén pertenezcan a la misma categoría
            if (!article.getCategory().getId().equals(storage.getCategory().getId())) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new APIResponse("El artículo y el almacén no pertenecen a la misma categoría", true, HttpStatus.BAD_REQUEST));
            }

            if (assignQuantity <= 0) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new APIResponse("La cantidad a asignar debe ser mayor a 0", true, HttpStatus.BAD_REQUEST));
            }

            if (article.getQuantity() < assignQuantity) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new APIResponse("Cantidad del artículo insuficiente", true, HttpStatus.BAD_REQUEST));
            }

            // Disminuye la cantidad del artículo
            article.setQuantity(article.getQuantity() - assignQuantity);
            articleRepository.save(article);

            // Por cada unidad a asignar se crea un registro en la tabla de intersección
            for (int i = 0; i < assignQuantity; i++) {
                StorageHasArticle sha = new StorageHasArticle(article, storage);
                storageHasArticleRepository.save(sha);
            }

            return ResponseEntity.ok(new APIResponse("Asignación realizada correctamente", null, false, HttpStatus.OK));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new APIResponse("Error al asignar artículo al almacén", true, HttpStatus.INTERNAL_SERVER_ERROR));
        }
    }

}
