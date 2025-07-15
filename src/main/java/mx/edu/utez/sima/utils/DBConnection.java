package mx.edu.utez.sima.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;

/*
* @Configuration: Le dice a Spring que esta clase de Java va a
* generar una configuracion durante la ejecucionde la aplicacion
* pero esta anotacion debe siempre de ir con un metodo con la anotacion
* bean que le diga que va a configurar
*
* @Bean: Le indica a Spring el metodo que retornara dicha configuracion
*/
@Configuration
public class DBConnection {
    @Value("${db.url}")
    private String DB_URL;

    @Value("${db.username}")
    private String DB_USERNAME;

    @Value("${db.password}")
    private String DB_PASSWORD;


    @Bean
    public DataSource getConnection(){
        try {
            DriverManagerDataSource configuration = new DriverManagerDataSource();
            configuration.setUrl(DB_URL);
            configuration.setUsername(DB_USERNAME);
            configuration.setPassword(DB_PASSWORD);
            /*
            * 1: como si fuera pagina: com.mysql
            * 2: GTA SA: cj
            * 3: Protocolo de base de datos: jdbc
            * 4: Clase: Driver
            * */
            configuration.setDriverClassName("com.mysql.cj.jdbc.Driver");

            return configuration;
        } catch (Exception e) {
            // Manejo de excepciones
            System.out.println("Error al establecer la conexión a la base de datos: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
}
