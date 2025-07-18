package mx.edu.utez.sima.modules.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<BeanUser, Long> {

    // Buscar por UUID
    Optional<BeanUser> findByUuid(String uuid);

    // Buscar por username
    Optional<BeanUser> findByUsername(String username);

    // Buscar por email
    Optional<BeanUser> findByEmail(String email);

    // Verificar si existe por username
    boolean existsByUsername(String username);

    // Verificar si existe por email
    boolean existsByEmail(String email);

    // Buscar por rol
    List<BeanUser> findByRolId(Long rolId);

    // Buscar por nombre de rol
    @Query("SELECT u FROM BeanUser u WHERE u.rol.name = :rolName")
    List<BeanUser> findByRolName(@Param("rolName") String rolName);

    // Buscar usuarios activos
    List<BeanUser> findByActiveTrue();

    // Buscar usuarios inactivos
    List<BeanUser> findByActiveFalse();

    // Buscar usuarios por estado
    List<BeanUser> findByActive(Boolean active);

    // Buscar responsables de almacén sin almacén asignado
    @Query("SELECT u FROM BeanUser u WHERE u.rol.name = :rolName AND u.storage IS NULL")
    List<BeanUser> findByRolNameAndStorageIsNull(@Param("rolName") String rolName);

    // Buscar usuarios con almacén asignado
    @Query("SELECT u FROM BeanUser u WHERE u.storage IS NOT NULL")
    List<BeanUser> findByStorageIsNotNull();

    // Buscar usuarios sin almacén asignado
    @Query("SELECT u FROM BeanUser u WHERE u.storage IS NULL")
    List<BeanUser> findByStorageIsNull();

    // Buscar por nombre o apellido
    @Query("SELECT u FROM BeanUser u WHERE u.name LIKE %:name% OR u.lastName LIKE %:name%")
    List<BeanUser> findByNameContainingOrLastNameContaining(@Param("name") String name);

    // Buscar por rol y estado
    List<BeanUser> findByRolIdAndActive(Long rolId, Boolean active);

    // Contar usuarios por rol
    Long countByRolId(Long rolId);

    // Contar usuarios activos
    Long countByActiveTrue();

    // Buscar usuarios creados en un rango de fechas
    @Query("SELECT u FROM BeanUser u WHERE u.createdAt BETWEEN :startDate AND :endDate")
    List<BeanUser> findByCreatedAtBetween(@Param("startDate") java.time.LocalDateTime startDate,
                                          @Param("endDate") java.time.LocalDateTime endDate);
}
