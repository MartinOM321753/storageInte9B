package utez.edu.mx.unidad3.modules.article;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ArticleRepository extends JpaRepository< Article, Long> {

    List<Article> findByName(String name);
    List<Article> findByCategory(String category);
}
