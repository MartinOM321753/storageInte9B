package utez.edu.mx.unidad3.modules.storage;

import org.springframework.data.jpa.repository.JpaRepository;

public interface StorageReporitory extends JpaRepository< Storage, Long> {
    Storage findByName(String name);

}
