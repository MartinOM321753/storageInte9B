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

import java.util.*;

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
    private UserRepository userRepository;

    @Override
    public void run(String... args) throws Exception {


        // Creamos los roles con nombre y los guardamos si no existen
        Rol rolAdmin = rolRepository.findByName("ADMIN");
        if (rolAdmin == null) {
            rolAdmin = new Rol();
            rolAdmin.setName("ADMIN");
            rolAdmin = rolRepository.save(rolAdmin);
        }

        Rol rolUser = rolRepository.findByName("USER");
        if (rolUser == null) {
            rolUser = new Rol();
            rolUser.setName("USER");
            rolUser = rolRepository.save(rolUser);
        }

// Creamos los usuarios y les asignamos el rol correcto manualmente
        BeanUser admin = new BeanUser();
        admin.setUsername("admin");
        admin.setName("Admin");
        admin.setLastName("Apellidos");
        admin.setEmail("admin@gmail.com");
        admin.setPassword("password123");
        admin.setActive(true);
        admin.setRol(rolAdmin);
        ResponseEntity<?> respAdmin = userService.createUser(admin);
        System.out.println(" Usuario creado: admin - " + respAdmin.getBody());

        BeanUser users = new BeanUser();
        users.setUsername("user");
        users.setName("User");
        users.setLastName("Apellidos");
        users.setEmail("user@gmail.com");
        users.setPassword("password123");
        users.setActive(true);
        users.setRol(rolUser);
        ResponseEntity<?> respUser = userService.createUser(users);
        System.out.println(" Usuario creado: user - " + respUser.getBody());



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
            List<BeanUser> userss = userRepository.findByRolName("USER");

            if (!userss.isEmpty()) {
                BeanUser user = userss.get(0); // Tomar el primer usuario con rol USER

                // Crear 2 almacenes de ejemplo
                List<String> storageNames = Arrays.asList("Almacén Tech 1", "Almacén Tech 2");

                for (String storageName : storageNames) {
                    Storage storage = new Storage();
                    storage.setCategory(techCategory);

                    // CORRECCIÓN: Usar el usuario completo obtenido de la base de datos
                    storage.setResponsible(user);

                    ResponseEntity<?> respStorage = storageService.createStorage(storage);
                    APIResponse apiResp = (APIResponse) respStorage.getBody();

                    if (apiResp != null && apiResp.getData() instanceof Storage) {
                        techStorages.add((Storage) apiResp.getData());
                        System.out.println("Almacén insertado exitosamente: " + storageName + " - " + respStorage.getBody());
                    } else {
                        System.out.println("Error al insertar almacén: " + storageName + " - " + respStorage.getBody());
                    }
                }
            } else {
                System.out.println("No se encontraron usuarios con rol USER para asignar a los almacenes.");
            }
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
                APIResponse articleApiResp = respArticle.getBody();

                if (articleApiResp != null  &&
                        articleApiResp.getData() instanceof Article && !techStorages.isEmpty()) {

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

        // Creación de 5 artículos con categoría diferente ("Materia")
        Optional<Category> otherCategoryOpt = categoryRepository.findByCategoryName("Materia");
        if (otherCategoryOpt.isPresent()) {
            Category otherCategory = otherCategoryOpt.get();
            for (int i = 1; i <= 5; i++) {
                Article article = new Article();
                article.setArticleName("Articulo Materia " + i);
                article.setDescription("Descripción del articulo Materia " + i);
                article.setQuantity(50L);
                article.setCategory(otherCategory);

                // No se asigna almacén
                ResponseEntity<?> respArticle = articleService.createArticle(article);
                System.out.println("Articulo creado (sin almacén): Articulo Materia " + i + " - " + respArticle.getBody());
            }
        } else {
            System.out.println("No se encontró la categoría 'Materia' para los artículos sin asignar a almacenes.");
        }
    }
}
