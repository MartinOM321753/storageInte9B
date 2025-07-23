package mx.edu.utez.sima.modules.rol;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RolRepository extends JpaRepository<Rol, Long> {

    // Buscar por nombre
    Rol findByName(String name);

    // Verificar si existe por nombre
    boolean existsByName(String name);

    // Buscar por nombre que contenga texto
    List<Rol> findByNameContainingIgnoreCase(String name);

    // Buscar roles que tengan usuarios asignados
    @Query("SELECT r FROM Rol r WHERE SIZE(r.user) > 0")
    List<Rol> findByUserIsNotEmpty();

    // Buscar roles sin usuarios asignados
    @Query("SELECT r FROM Rol r WHERE SIZE(r.user) = 0")
    List<Rol> findByUserIsEmpty();

    // Contar usuarios por rol
    @Query("SELECT COUNT(u) FROM BeanUser u WHERE u.rol.id = :rolId")
    Long countUsersByRolId(@Param("rolId") Long rolId);



    // Buscar roles específicos (útil para obtener roles predefinidos)
    @Query("SELECT r FROM Rol r WHERE r.name IN :roleNames")
    List<Rol> findByNameIn(@Param("roleNames") List<String> roleNames);
}
