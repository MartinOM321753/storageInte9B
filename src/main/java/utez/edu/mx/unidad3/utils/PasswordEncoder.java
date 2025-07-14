package utez.edu.mx.unidad3.utils;

import org.apache.tomcat.util.buf.UEncoder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordEncoder {
    public static String encodePassword(String rawPassword) {
        return  new BCryptPasswordEncoder().encode(rawPassword); // Reemplaza esto con la lógica real de codificación
    }

    public static boolean verifyPassword(String rawPassword, String encodedPassword) {
        // Aquí puedes implementar la lógica para verificar la contraseña
        // Por ejemplo, comparando el hash de la contraseña sin codificar con el hash almacenado
        return new BCryptPasswordEncoder().matches(rawPassword, encodedPassword);
    }

    public static void main(String[] args) {

        System.out.println(
                new PasswordEncoder().encodePassword("root")
        );

    }
}
