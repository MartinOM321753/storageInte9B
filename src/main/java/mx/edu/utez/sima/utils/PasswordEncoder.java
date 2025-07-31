package mx.edu.utez.sima.utils;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordEncoder {
    public static String encodePassword(String rawPassword) {
        return  new BCryptPasswordEncoder().encode(rawPassword);
    }

    public static boolean verifyPassword(String rawPassword, String encodedPassword) {
        return new BCryptPasswordEncoder().matches(rawPassword, encodedPassword);
    }

    public static void main(String[] args) {

        System.out.println(
                new PasswordEncoder().encodePassword("root")
        );

    }
}
