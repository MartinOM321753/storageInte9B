package mx.edu.utez.sima.modules.storageHasArticle;


import jakarta.persistence.*;
import mx.edu.utez.sima.modules.article.Article;
import mx.edu.utez.sima.modules.storage.Storage;

@Entity
@Table(name = "storage_has_articles")
public class StorageHasArticle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "article_id", nullable = false)
    private Article article;

    @ManyToOne
    @JoinColumn(name = "storage_id", nullable = false)
    private Storage storage;

    public StorageHasArticle() {
    }

    public StorageHasArticle(Article article, Storage storage) {
        this.article = article;
        this.storage = storage;
    }

    public Long getId() {
        return id;
    }

    public Article getArticle() {
        return article;
    }

    public void setArticle(Article article) {
        this.article = article;
    }

    public Storage getStorage() {
        return storage;
    }

    public void setStorage(Storage storage) {
        this.storage = storage;
    }
}