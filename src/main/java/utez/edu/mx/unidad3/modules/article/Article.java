package utez.edu.mx.unidad3.modules.article;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import utez.edu.mx.unidad3.modules.StorageHasArticles.StorageHasArticles;
import utez.edu.mx.unidad3.modules.category.Category;
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    @OneToMany(mappedBy = "article")
    @JsonIgnore
    private List<StorageHasArticles> storageHasArticles;


    public Article() {
    }

    public Article(Long id, String uuid, String articleName, String description, Category category, List<StorageHasArticles> storageHasArticles) {
        this.id = id;
        this.uuid = uuid;
        this.articleName = articleName;
        this.description = description;
        this.category = category;
        this.storageHasArticles = storageHasArticles;
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

    public List<StorageHasArticles> getStorageHasArticles() {
        return storageHasArticles;
    }

    public void setStorageHasArticles(List<StorageHasArticles> storageHasArticles) {
        this.storageHasArticles = storageHasArticles;
    }
}