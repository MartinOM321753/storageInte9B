package utez.edu.mx.unidad3.modules.StorageHasArticles;


import jakarta.persistence.*;
import utez.edu.mx.unidad3.modules.article.Article;
import utez.edu.mx.unidad3.modules.storage.Storage;

import java.time.LocalDateTime;

@Entity
@Table(name = "storage_has_articles")
public class StorageHasArticles {

    @EmbeddedId
    private StorageHasArticlesId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("storageId")
    @JoinColumn(name = "storage_id")
    private Storage storage;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("articleId")
    @JoinColumn(name = "article_id")
    private Article article;

    @Column(name = "quantity", nullable = false)
    private Integer quantity = 1;

    @Column(name = "entry_date", nullable = false)
    private LocalDateTime entryDate;

    @PrePersist
    protected void onCreate() {
        entryDate = LocalDateTime.now();
    }
}