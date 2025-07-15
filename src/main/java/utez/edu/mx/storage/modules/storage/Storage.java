package utez.edu.mx.storage.modules.storage;

import jakarta.persistence.*;
import utez.edu.mx.storage.modules.article.Article;
import utez.edu.mx.storage.modules.category.Category;
import utez.edu.mx.storage.modules.user.BeanUser;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "storage")
public class Storage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "uuid", nullable = false, unique = true)
    private String uuid;

    @Column(name = "storage_identifier", nullable = false, unique = true, length = 20)
    private String storageIdentifier;

    @Column(name = "status", nullable = false)
    private Boolean status;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "responsible_id", unique = true)
    private BeanUser responsible;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    @ManyToMany
    @JoinTable(
            name = "storage_has_articles",
            joinColumns = @JoinColumn(name = "storage_id"),
            inverseJoinColumns = @JoinColumn(name = "article_id")
    )
    private List<Article> articles = new ArrayList<>();


    public Storage() {
    }

    public Storage(Long id, String uuid, String storageIdentifier, Boolean status, BeanUser responsible, Category category, List<Article> articles) {
        this.id = id;
        this.uuid = uuid;
        this.storageIdentifier = storageIdentifier;
        this.status = status;
        this.responsible = responsible;
        this.category = category;
        this.articles = articles;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public List<Article> getArticles() {
        return articles;
    }

    public void setArticles(List<Article> articles) {
        this.articles = articles;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getStorageIdentifier() {
        return storageIdentifier;
    }

    public void setStorageIdentifier(String storageIdentifier) {
        this.storageIdentifier = storageIdentifier;
    }

    public BeanUser getResponsible() {
        return responsible;
    }

    public void setResponsible(BeanUser responsible) {
        this.responsible = responsible;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }


}