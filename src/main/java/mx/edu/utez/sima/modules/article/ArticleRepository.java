package mx.edu.utez.sima.modules.article;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

@Repository
public interface ArticleRepository extends JpaRepository<Article, Long> {

    // Buscar por UUID
    Optional<Article> findByUuid(String uuid);

    // Buscar por nombre de artículo
    List<Article> findByArticleNameContainingIgnoreCase(String articleName);

    // Buscar por nombre exacto
    Optional<Article> findByArticleName(String articleName);

    // Buscar por categoría
    List<Article> findByCategoryId(Long categoryId);

    // Buscar por cantidad específica
    List<Article> findByQuantity(Long quantity);

    // Buscar por cantidad menor que
    List<Article> findByQuantityLessThan(Long quantity);

    // Buscar por cantidad mayor que
    List<Article> findByQuantityGreaterThan(Long quantity);

    // Buscar por cantidad entre rangos
    List<Article> findByQuantityBetween(Long minQuantity, Long maxQuantity);

    // Buscar artículos en un almacén específico
    @Query("SELECT a FROM Article a JOIN a.storages s WHERE s.id = :storageId")
    List<Article> findByStoragesId(@Param("storageId") Long storageId);

    // Buscar artículos no asignados a ningún almacén
    @Query("SELECT a FROM Article a WHERE SIZE(a.storages) = 0")
    List<Article> findByStoragesIsEmpty();

    // Buscar artículos asignados a almacenes
    @Query("SELECT a FROM Article a WHERE SIZE(a.storages) > 0")
    List<Article> findByStoragesIsNotEmpty();

    // Buscar por nombre y categoría
    List<Article> findByArticleNameContainingIgnoreCaseAndCategoryId(String articleName, Long categoryId);

    // Buscar por descripción
    List<Article> findByDescriptionContainingIgnoreCase(String description);

    // Contar artículos por categoría
    Long countByCategoryId(Long categoryId);

    // Sumar todas las cantidades
    @Query("SELECT SUM(a.quantity) FROM Article a")
    Long sumAllQuantities();

    // Sumar cantidades por categoría
    @Query("SELECT SUM(a.quantity) FROM Article a WHERE a.category.id = :categoryId")
    Long sumQuantityByCategoryId(@Param("categoryId") Long categoryId);

    // Buscar artículos por nombre de categoría
    @Query("SELECT a FROM Article a WHERE a.category.categoryName = :categoryName")
    List<Article> findByCategoryName(@Param("categoryName") String categoryName);

    // Buscar artículos sin stock (cantidad = 0)
    @Query("SELECT a FROM Article a WHERE a.quantity = 0")
    List<Article> findWithoutStock();

    // Buscar artículos con stock bajo (menor al umbral)
    @Query("SELECT a FROM Article a WHERE a.quantity < :threshold AND a.quantity > 0")
    List<Article> findWithLowStock(@Param("threshold") Long threshold);

    // Buscar artículos con stock suficiente (mayor o igual al umbral)
    @Query("SELECT a FROM Article a WHERE a.quantity >= :threshold")
    List<Article> findWithSufficientStock(@Param("threshold") Long threshold);

    // Buscar artículos ordenados por nombre
    List<Article> findAllByOrderByArticleNameAsc();

    // Buscar artículos ordenados por cantidad
    List<Article> findAllByOrderByQuantityDesc();

    // Buscar artículos por múltiples almacenes
    @Query("SELECT DISTINCT a FROM Article a JOIN a.storages s WHERE s.id IN :storageIds")
    List<Article> findByStoragesIdIn(@Param("storageIds") List<Long> storageIds);

    // Buscar artículos de una categoría en un almacén específico
    @Query("SELECT a FROM Article a JOIN a.storages s WHERE a.category.id = :categoryId AND s.id = :storageId")
    List<Article> findByCategoryIdAndStoragesId(@Param("categoryId") Long categoryId, @Param("storageId") Long storageId);

    // Buscar artículos que no están en un almacén específico
    @Query("SELECT a FROM Article a WHERE a.id NOT IN (SELECT DISTINCT a2.id FROM Article a2 JOIN a2.storages s WHERE s.id = :storageId)")
    List<Article> findNotInStorage(@Param("storageId") Long storageId);

    // Contar artículos en un almacén específico
    @Query("SELECT COUNT(a) FROM Article a JOIN a.storages s WHERE s.id = :storageId")
    Long countByStoragesId(@Param("storageId") Long storageId);

    // Buscar artículos por rango de cantidad y categoría
    @Query("SELECT a FROM Article a WHERE a.quantity BETWEEN :minQuantity AND :maxQuantity AND a.category.id = :categoryId")
    List<Article> findByQuantityBetweenAndCategoryId(@Param("minQuantity") Long minQuantity,
                                                     @Param("maxQuantity") Long maxQuantity,
                                                     @Param("categoryId") Long categoryId);

    // Verificar si existe artículo por nombre
    boolean existsByArticleName(String articleName);

    // Buscar artículos que pueden ser agregados a un almacén (misma categoría)
    @Query("SELECT a FROM Article a WHERE a.category.id = :categoryId AND a.id NOT IN (SELECT DISTINCT a2.id FROM Article a2 JOIN a2.storages s WHERE s.id = :storageId)")
    List<Article> findAvailableForStorage(@Param("categoryId") Long categoryId, @Param("storageId") Long storageId);
}
