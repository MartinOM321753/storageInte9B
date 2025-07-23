package mx.edu.utez.sima.utils;

import mx.edu.utez.sima.modules.article.Article;
import mx.edu.utez.sima.modules.category.Category;
import mx.edu.utez.sima.modules.category.CategoryRepository;
import mx.edu.utez.sima.modules.rol.Rol;
import mx.edu.utez.sima.modules.rol.RolRepository;
import mx.edu.utez.sima.modules.storage.Storage;
import mx.edu.utez.sima.modules.storage.StorageRepository;
import mx.edu.utez.sima.modules.user.BeanUser;
import mx.edu.utez.sima.modules.user.UserRepository;
import mx.edu.utez.sima.services.ArticleService;
import mx.edu.utez.sima.services.CategoryService;
import mx.edu.utez.sima.services.StorageService;
import mx.edu.utez.sima.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Configuration
public class InitialConfig implements CommandLineRunner {

    @Autowired
    private UserService userService;

    @Autowired
    private RolRepository rolRepository;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private StorageService storageService;

    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private ArticleService articleService;
    @Autowired
    private StorageRepository storageRepository;
    @Autowired
    private UserRepository userRepository;


    @Override
    public void run(String... args) throws Exception {

        // Inicialización de roles
        List<String> roles = Arrays.asList("ADMIN", "USER");
        roles.forEach(roleName -> {
            if (!rolRepository.existsByName(roleName)) {
                Rol rol = new Rol();
                rol.setName(roleName);
                rolRepository.save(rol);
                System.out.println("Rol creado: " + roleName);
            }
        });


        List<String> users = Arrays.asList("Admin", "user");
        users.forEach(userName -> {
            BeanUser user = new BeanUser();
            user.setUsername(userName.toLowerCase());
            user.setName(userName);
            user.setLastName("LastName " + userName);
            user.setEmail(userName.toLowerCase() + "@example.com");
            user.setPassword("password123");
            user.setActive(true);
            user.setRol(rolRepository.findByName(userName.toUpperCase()));
            ResponseEntity<?> respUser = userService.createUser(user);
            System.out.println("Usuario creado: " + user.getUsername() + " - " + respUser.getBody());

        });



        // Inserción de categorías iniciales
        List<String> categorias = Arrays.asList(
                "Tecnología", "Materia", "Muebles", "Ropa", "Textiles", "Alimentos",
                "Automotriz", "Construcción", "Farmacéutico", "Químicos", "Electrónica",
                "Metal", "Plástico", "Papel", "Madera", "Libros", "Deportes", "Hogar",
                "Juguetes", "Herramientas"
        );
        categorias.forEach(nombre -> {
            ResponseEntity<?> respCategoria = categoryService.createCategory(nombre);
            System.out.println("Categoría insertada: " + nombre + " - " + respCategoria.getBody());
        });

        // Inserción de registros de storage (usando categoría "Tecnología")
        Optional<Category> techCategoryOpt = categoryRepository.findByCategoryName("Tecnología");
        List<Storage> techStorages = new ArrayList<>();
        if (techCategoryOpt.isPresent()) {
            Category techCategory = techCategoryOpt.get();
            List<String> storageCodes = Arrays.asList("", "");
          BeanUser  user =  userRepository.findByRolName("USER").getFirst();

            storageCodes.forEach(code -> {
                Storage storage = new Storage();
                storage.setResponsible(user);
                storage.setCategory(techCategory);
                ResponseEntity<?> respStorage = storageService.createStorage(storage);
                APIResponse apiResp = (APIResponse) respStorage.getBody();
                if (apiResp.getData() instanceof Storage) {
                    techStorages.add((Storage) apiResp.getData());
                }
                System.out.println("Almacén insertado: " + code + " - " + respStorage.getBody());
            });
        } else {
            System.out.println("No se encontró la categoría Tecnología para asignar a los almacenes.");
        }

        // Creación de 5 artículos con la misma categoría de los almacenes ("Tecnología")
        if (techCategoryOpt.isPresent()) {
            Category techCategory = techCategoryOpt.get();
            for (int i = 1; i <= 5; i++) {
                Article article = new Article();
                article.setArticleName("Articulo Tech " + i);
                article.setDescription("Descripción del artículo Tech " + i);
                article.setQuantity(10L);
                article.setCategory(techCategory);

                // Se crea el artículo
                ResponseEntity<APIResponse> respArticle = articleService.createArticle(article);
                APIResponse articleApiResp = (APIResponse) respArticle.getBody();
                if (articleApiResp.getData() instanceof Article && !techStorages.isEmpty()) {
                    Article savedArticle = (Article) articleApiResp.getData();
                    // Se asignan 5 unidades del artículo al primer almacén disponible
                    ResponseEntity<APIResponse> assignResp = articleService.assignArticleToStorage(
                            savedArticle.getId(),
                            techStorages.get(0).getId(),
                            5L
                    );
                    System.out.println("Asignación de Articulo Tech " + i + " a almacén: " + assignResp.getBody());
                }
            }
        }

        // Creaci\\ón de 5 art\\u00edculos con categor\\ía diferente ("Materia")
        Optional<Category> otherCategoryOpt = categoryRepository.findByCategoryName("Materia");
        if (otherCategoryOpt.isPresent()) {
            Category otherCategory = otherCategoryOpt.get();
            for (int i = 1; i <= 5; i++) {
                Article article = new Article();
                article.setArticleName("Articulo Materia " + i);
                article.setDescription("Descripción del articulo Materia " + i);
                article.setQuantity(50L);
                article.setCategory(otherCategory);
                // No se asigna almacen
                ResponseEntity<?> respArticle = articleService.createArticle(article);
                System.out.println("articulo creado (sin almacén): Articulo Materia " + i + " - " + respArticle.getBody());
            }
        } else {
            System.out.println("No se encontro la categoria 'Materia' para los articulos sin asignar a almacenes.");
        }
    }
}