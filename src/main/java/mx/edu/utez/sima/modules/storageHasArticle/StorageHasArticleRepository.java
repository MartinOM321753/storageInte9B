package mx.edu.utez.sima.modules.storageHasArticle;


import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StorageHasArticleRepository extends JpaRepository<StorageHasArticle, Long> {

    List<StorageHasArticle> findByArticleIdAndStorageId(Long article, Long storage);

    List<StorageHasArticle> findByStorageId(Long storageId);

}
