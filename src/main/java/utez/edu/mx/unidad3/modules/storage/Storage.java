package utez.edu.mx.unidad3.modules.storage;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import utez.edu.mx.unidad3.modules.StorageHasArticles.StorageHasArticles;
import utez.edu.mx.unidad3.modules.category.Category;
import utez.edu.mx.unidad3.modules.user.BeanUser;

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

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "responsible_id", unique = true)
    private BeanUser responsible;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    @OneToMany(mappedBy = "storage")
    @JsonIgnore
    private List<StorageHasArticles> storageHasArticles;
}