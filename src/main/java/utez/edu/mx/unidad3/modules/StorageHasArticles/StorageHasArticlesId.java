package utez.edu.mx.unidad3.modules.StorageHasArticles;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class StorageHasArticlesId implements Serializable {

    @Column(name = "storage_id")
    private Long storageId;

    @Column(name = "article_id")
    private Long articleId;

    public StorageHasArticlesId() {}

    public StorageHasArticlesId(Long storageId, Long articleId) {
        this.storageId = storageId;
        this.articleId = articleId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof StorageHasArticlesId)) return false;
        StorageHasArticlesId that = (StorageHasArticlesId) o;
        return Objects.equals(storageId, that.storageId) &&
                Objects.equals(articleId, that.articleId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(storageId, articleId);
    }
}