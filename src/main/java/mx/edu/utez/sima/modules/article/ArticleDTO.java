package mx.edu.utez.sima.modules.article;

import jakarta.validation.constraints.*;
import mx.edu.utez.sima.modules.category.Category;
public class ArticleDTO {

    private Long id;
    @NotBlank(message = "El nombre del artículo es obligatorio")
    @Size(max = 100, message = "El nombre del artículo no puede exceder los 100 caracteres")
    private String articleName;
    @NotBlank(message = "La descripción es obligatoria")
    @Size(max = 255, message = "La descripción no puede exceder los 255 caracteres")
    private String description;
    @NotNull(message = "La cantidad es obligatoria")
    @Min(value = 1, message = "La cantidad debe ser al menos 1")
    private Long quantity;

    private Long category;

    public Article toEntity() {
        Article article = new Article();
        article.setArticleName(this.articleName);
        article.setDescription(this.description);
        article.setQuantity(this.quantity);
        Category category = new Category();
        category.setId(this.category);
        article.setCategory(category);
        return article;
    }
    public Article toEntityUpdate() {
        Article article = new Article();
        article.setId(this.id);
        article.setArticleName(this.articleName);
        article.setDescription(this.description);
        article.setQuantity(this.quantity);
        Category category = new Category();
        category.setId(this.category);
        article.setCategory(category);
        return article;
    }

    public ArticleDTO(Long id, String articleName, String description, Long quantity, Long category) {
        this.id = id;
        this.articleName = articleName;
        this.description = description;
        this.quantity = quantity;
        this.category = category;
    }

    public Long getCategory() {
        return category;
    }

    public void setCategory(Long category) {
        this.category = category;
    }

    public Long getQuantity() {
        return quantity;
    }

    public void setQuantity(Long quantity) {
        this.quantity = quantity;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getArticleName() {
        return articleName;
    }

    public void setArticleName(String articleName) {
        this.articleName = articleName;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
