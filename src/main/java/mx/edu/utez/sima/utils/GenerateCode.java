package mx.edu.utez.sima.utils;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GenerateCode {
    private static final String UPPERCASE = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String LOWERCASE = "abcdefghijklmnopqrstuvwxyz";
    private static final String DIGITS = "0123456789";
    private static final String SPECIAL = "!@#$%^&*()-_+?";
    private static final String ALL_CHARACTERS = UPPERCASE + LOWERCASE + DIGITS + SPECIAL;
    private static final int PASSWORD_LENGTH = 8;
    private static final SecureRandom random = new SecureRandom();

    public static String generateStorageIdentifier(Long id) {
        return "A-" + String.format("%03d", id);
    }

    public static String generatePassword() {
        StringBuilder password = new StringBuilder(PASSWORD_LENGTH);

        password.append(UPPERCASE.charAt(random.nextInt(UPPERCASE.length())));
        password.append(LOWERCASE.charAt(random.nextInt(LOWERCASE.length())));
        password.append(SPECIAL.charAt(random.nextInt(SPECIAL.length())));
        password.append(DIGITS.charAt(random.nextInt(DIGITS.length())));

        for (int i = 3; i < PASSWORD_LENGTH; i++) {
            password.append(ALL_CHARACTERS.charAt(random.nextInt(ALL_CHARACTERS.length())));
        }

        List<Character> passwordChars = new ArrayList<>();
        for (char c : password.toString().toCharArray()) {
            passwordChars.add(c);
        }
        Collections.shuffle(passwordChars);

        StringBuilder finalPassword = new StringBuilder();
        for (char c : passwordChars) {
            finalPassword.append(c);
        }

        return finalPassword.toString();
    }

    public static String generateUsername(String nombre, String apellido) {
        SecureRandom random = new SecureRandom();
        String cleanName = nombre.replaceAll("[^a-zA-Z]", "").toLowerCase();
        String cleanApellido = apellido.replaceAll("[^a-zA-Z]", "").toLowerCase();
        int number = random.nextInt(900) + 100; // número de 3 dígitos
        return cleanName + cleanApellido.charAt(0) + number;
    }
}
