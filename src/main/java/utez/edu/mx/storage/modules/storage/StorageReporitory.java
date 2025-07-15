package utez.edu.mx.storage.modules.storage;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StorageReporitory extends JpaRepository< Storage, Long> {
    Optional <Storage> findByStorageIdentifier(String identifier);

}
