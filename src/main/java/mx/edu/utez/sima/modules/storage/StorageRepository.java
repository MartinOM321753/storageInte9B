package mx.edu.utez.sima.modules.storage;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StorageRepository extends JpaRepository< Storage, Long> {
    // Buscar por UUID
    Optional<Storage> findByUuid(String uuid);

    // Buscar por identificador de almacén
    Optional<Storage> findByStorageIdentifier(String storageIdentifier);

    // Verificar si existe por identificador
    boolean existsByStorageIdentifier(String storageIdentifier);

    // Buscar por estado
    List<Storage> findByStatus(Boolean status);

    // Buscar almacenes activos
    List<Storage> findByStatusTrue();

    // Buscar almacenes inactivos
    List<Storage> findByStatusFalse();

    // Buscar por categoría
    List<Storage> findByCategoryId(Long categoryId);

    // Buscar por responsable
    Optional<Storage> findByResponsibleId(Long responsibleId);

    // Buscar almacenes sin responsable
    List<Storage> findByResponsibleIsNull();

    // Buscar almacenes con responsable
    List<Storage> findByResponsibleIsNotNull();

    // Buscar por identificador que contenga texto
    List<Storage> findByStorageIdentifierContainingIgnoreCase(String identifier);

    // Buscar almacenes por categoría y estado
    List<Storage> findByCategoryIdAndStatus(Long categoryId, Boolean status);

    // Buscar almacenes que contengan un artículo específico
    @Query("SELECT s FROM Storage s JOIN s.articles a WHERE a.id = :articleId")
    List<Storage> findByArticlesId(@Param("articleId") Long articleId);

    // Buscar almacenes que no contengan un artículo específico
    @Query("SELECT s FROM Storage s WHERE s.id NOT IN (SELECT DISTINCT s2.id FROM Storage s2 JOIN s2.articles a WHERE a.id = :articleId)")
    List<Storage> findByArticlesIdNot(@Param("articleId") Long articleId);

    // Contar almacenes por categoría
    Long countByCategoryId(Long categoryId);

    // Contar almacenes activos
    Long countByStatusTrue();

    // Contar almacenes con responsable
    Long countByResponsibleIsNotNull();

    // Buscar almacenes con artículos
    @Query("SELECT s FROM Storage s WHERE SIZE(s.articles) > 0")
    List<Storage> findByArticlesIsNotEmpty();

    // Buscar almacenes sin artículos
    @Query("SELECT s FROM Storage s WHERE SIZE(s.articles) = 0")
    List<Storage> findByArticlesIsEmpty();

    // Buscar almacenes por nombre de categoría
    @Query("SELECT s FROM Storage s WHERE s.category.categoryName = :categoryName")
    List<Storage> findByCategoryName(@Param("categoryName") String categoryName);

    // Buscar almacenes por nombre de responsable
    @Query("SELECT s FROM Storage s WHERE s.responsible.name LIKE %:responsibleName% OR s.responsible.lastName LIKE %:responsibleName%")
    List<Storage> findByResponsibleNameContaining(@Param("responsibleName") String responsibleName);

    // Contar artículos en un almacén
    @Query("SELECT COUNT(a) FROM Storage s JOIN s.articles a WHERE s.id = :storageId")
    Long countArticlesByStorageId(@Param("storageId") Long storageId);

    // Buscar almacenes ordenados por identificador
    List<Storage> findAllByOrderByStorageIdentifierAsc();

    // Buscar almacenes de una categoría específica que estén activos
    @Query("SELECT s FROM Storage s WHERE s.category.id = :categoryId AND s.status = true")
    List<Storage> findByCategoryIdAndStatusTrue(@Param("categoryId") Long categoryId);
}
