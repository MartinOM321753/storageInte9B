package mx.edu.utez.sima.modules.article;


import jakarta.persistence.*;
import mx.edu.utez.sima.modules.category.Category;
import mx.edu.utez.sima.modules.storage.Storage;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "article")
public class Article {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "uuid", nullable = false, unique = true)
    private String uuid;

    @Column(name = "article_name", nullable = false, length = 100)
    private String articleName;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "quantity", nullable = false )
    private Long quantity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;



    @ManyToMany(mappedBy = "articles")
    private List<Storage> storages = new ArrayList<>();

    public Article() {
    }

    public Article(Long id, String uuid, String articleName, String description, Long quantity, Category category, List<Storage> storages) {
        this.id = id;
        this.uuid = uuid;
        this.articleName = articleName;
        this.description = description;
        this.quantity = quantity;
        this.category = category;
        this.storages = storages;
    }

    public Long getQuantity() {
        return quantity;
    }

    public void setQuantity(Long quantity) {
        this.quantity = quantity;
    }

    public List<Storage> getStorages() {
        return storages;
    }

    public void setStorages(List<Storage> storages) {
        this.storages = storages;
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

    public String getArticleName() {
        return articleName;
    }

    public void setArticleName(String articleName) {
        this.articleName = articleName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }


}