package mx.edu.utez.sima.modules.category;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository< Category, Long> {
    // Buscar por UUID
    Optional<Category> findByUuid(String uuid);

    // Buscar por nombre de categoría
    Optional<Category> findByCategoryName(String categoryName);

    // Verificar si existe por nombre
    boolean existsByCategoryName(String categoryName);

    // Buscar categorías que contengan un texto en el nombre
    List<Category> findByCategoryNameContainingIgnoreCase(String categoryName);

    // Buscar categorías con almacenes asociados
    @Query("SELECT c FROM Category c WHERE SIZE(c.storages) > 0")
    List<Category> findByStoragesIsNotEmpty();

    // Buscar categorías sin almacenes asociados
    @Query("SELECT c FROM Category c WHERE SIZE(c.storages) = 0")
    List<Category> findByStoragesIsEmpty();

    // Buscar categorías con artículos asociados
    @Query("SELECT c FROM Category c WHERE SIZE(c.articles) > 0")
    List<Category> findByArticlesIsNotEmpty();

    // Buscar categorías sin artículos asociados
    @Query("SELECT c FROM Category c WHERE SIZE(c.articles) = 0")
    List<Category> findByArticlesIsEmpty();

    // Contar almacenes por categoría
    @Query("SELECT COUNT(s) FROM Storage s WHERE s.category.id = :categoryId")
    Long countStoragesByCategoryId(@Param("categoryId") Long categoryId);

    // Contar artículos por categoría
    @Query("SELECT COUNT(a) FROM Article a WHERE a.category.id = :categoryId")
    Long countArticlesByCategoryId(@Param("categoryId") Long categoryId);

    // Buscar categorías creadas en un rango de fechas
    @Query("SELECT c FROM Category c WHERE c.createdAt BETWEEN :startDate AND :endDate")
    List<Category> findByCreatedAtBetween(@Param("startDate") java.time.LocalDateTime startDate,
                                          @Param("endDate") java.time.LocalDateTime endDate);

    // Buscar categorías con almacenes activos
    @Query("SELECT DISTINCT c FROM Category c JOIN c.storages s WHERE s.status = true")
    List<Category> findByStoragesStatusTrue();
}
