package utez.edu.mx.unidad3.modules.category;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import utez.edu.mx.unidad3.modules.article.Article;
import utez.edu.mx.unidad3.modules.storage.Storage;

import java.time.LocalDateTime;
import java.util.List;


@Entity
@Table(name = "category")
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "uuid", nullable = false, unique = true)
    private String uuid;

    @Column(name = "category_name", nullable = false, unique = true, length = 100)
    private String categoryName;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "category")
    @JsonIgnore
    private List<Storage> storages;

    @OneToMany(mappedBy = "category")
    @JsonIgnore
    private List<Article> articles;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}