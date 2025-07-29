package mx.edu.utez.sima.services;

import mx.edu.utez.sima.modules.Email.Emails;
import mx.edu.utez.sima.modules.rol.Rol;
import mx.edu.utez.sima.modules.rol.RolRepository;
import mx.edu.utez.sima.modules.user.BeanUser;
import mx.edu.utez.sima.modules.user.UserRepository;
import mx.edu.utez.sima.utils.APIResponse;
import mx.edu.utez.sima.utils.GenerateCode;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Gatherer;

@Service
public class UserService {

    private static final Logger logger = LogManager.getLogger(UserService.class);

    private final UserRepository userRepository;
    private final RolRepository rolRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;

    public UserService(UserRepository userRepository, RolRepository rolRepository, PasswordEncoder passwordEncoder, EmailService emailService) {
        this.userRepository = userRepository;
        this.rolRepository = rolRepository;
        this.passwordEncoder = passwordEncoder;
        this.emailService = emailService;
    }

    @Transactional(rollbackFor = Exception.class)
    public ResponseEntity<APIResponse> createUser(BeanUser user) {
        try {
            // Generar username si no viene desde frontend
            if (user.getUsername() == null || user.getUsername().isBlank()) {
                String generatedUsername;
                int intentos = 0;

                do {
                    generatedUsername = GenerateCode.generateUsername(user.getName(), user.getLastName());
                    intentos++;
                    if (intentos > 10) {
                        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                .body(new APIResponse("No se pudo generar un username único", true, HttpStatus.INTERNAL_SERVER_ERROR));
                    }
                } while (userRepository.existsByUsername(generatedUsername));

                user.setUsername(generatedUsername);
            }

            if (userRepository.existsByUsername(user.getUsername())) {
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body(new APIResponse("Usuario con este username ya existe", true, HttpStatus.CONFLICT));
            }

            if (userRepository.existsByEmail(user.getEmail())) {
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body(new APIResponse("Usuario con este email ya existe", true, HttpStatus.CONFLICT));
            }

            String newPass = GenerateCode.generatePassword();

            Emails email = new Emails(user.getEmail(), newPass, user.getUsername());
            Rol rol = rolRepository.findByName("USER");
            user.setRol(rol);
            user.setUuid(UUID.randomUUID().toString());

            user.setPassword(passwordEncoder.encode(newPass));
            BeanUser saved = userRepository.save(user);

            emailService.sendEmail(email, 2);

            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new APIResponse("Usuario creado exitosamente", saved, false, HttpStatus.CREATED));

        } catch (Exception e) {
            logger.error("Error al crear usuario: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new APIResponse("Error al crear usuario", true, HttpStatus.INTERNAL_SERVER_ERROR));
        }
    }


    // Obtener todos los usuarios
    @Transactional(readOnly = true)
    public ResponseEntity<APIResponse> getAllUsers() {
        try {
            List<BeanUser> users = userRepository.findAll();
            return ResponseEntity.ok(new APIResponse("Usuarios obtenidos", users, false, HttpStatus.OK));
        } catch (Exception e) {
            logger.error("Error al obtener usuarios: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new APIResponse("Error al obtener usuarios", true, HttpStatus.INTERNAL_SERVER_ERROR));
        }
    }

    // Obtener usuario por ID
    @Transactional(readOnly = true)
    public ResponseEntity<APIResponse> getUserById(Long id) {
        try {
            Optional<BeanUser> user = userRepository.findById(id);
            if (user.isPresent()) {
                return ResponseEntity.ok(new APIResponse("Usuario encontrado", user.get(), false, HttpStatus.OK));
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new APIResponse("Usuario no encontrado", true, HttpStatus.NOT_FOUND));
            }
        } catch (Exception e) {
            logger.error("Error al obtener usuario por ID: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new APIResponse("Error al obtener usuario", true, HttpStatus.INTERNAL_SERVER_ERROR));
        }
    }

    // Obtener usuario por UUID
    @Transactional(readOnly = true)
    public ResponseEntity<APIResponse> getUserByUuid(String uuid) {
        try {
            Optional<BeanUser> user = userRepository.findByUuid(uuid);
            if (user.isPresent()) {
                return ResponseEntity.ok(new APIResponse("Usuario encontrado", user.get(), false, HttpStatus.OK));
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new APIResponse("Usuario no encontrado", true, HttpStatus.NOT_FOUND));
            }
        } catch (Exception e) {
            logger.error("Error al obtener usuario por UUID: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new APIResponse("Error al obtener usuario", true, HttpStatus.INTERNAL_SERVER_ERROR));
        }
    }

    // Obtener usuario por username
    @Transactional(readOnly = true)
    public ResponseEntity<APIResponse> getUserByUsername(String username) {
        try {
            Optional<BeanUser> user = userRepository.findByUsername(username);
            if (user.isPresent()) {
                return ResponseEntity.ok(new APIResponse("Usuario encontrado", user.get(), false, HttpStatus.OK));
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new APIResponse("Usuario no encontrado", true, HttpStatus.NOT_FOUND));
            }
        } catch (Exception e) {
            logger.error("Error al obtener usuario por username: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new APIResponse("Error al obtener usuario", true, HttpStatus.INTERNAL_SERVER_ERROR));
        }
    }

    // Actualizar usuario
    @Transactional(rollbackFor = Exception.class)
    public ResponseEntity<APIResponse> updateUser(BeanUser userDetails) {
        try {
            if (userDetails.getId() == null) {
                // Validación agregada para evitar excepciones por id nulo
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new APIResponse("El ID del usuario es requerido para actualizar.", true, HttpStatus.BAD_REQUEST));
            }

            Optional<BeanUser> optional = userRepository.findById(userDetails.getId());
            if (optional.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new APIResponse("Usuario no encontrado", true, HttpStatus.NOT_FOUND));
            }

            BeanUser user = optional.get();

            if (!userDetails.getUsername().equals(user.getUsername())) {

                if (userRepository.existsByUsername(userDetails.getUsername())) {
                    return ResponseEntity.status(HttpStatus.CONFLICT)
                            .body(new APIResponse("Usuario con este username ya existe", true, HttpStatus.CONFLICT));
                }

            }
            if (!userDetails.getEmail().equals(user.getEmail())) {
                if (userRepository.existsByEmail(userDetails.getEmail())) {
                    return ResponseEntity.status(HttpStatus.CONFLICT)
                            .body(new APIResponse("Usuario con este email ya existe", true, HttpStatus.CONFLICT));
                }
            }

            user.setUsername(userDetails.getUsername());
            user.setName(userDetails.getName());
            user.setLastName(userDetails.getLastName());
            user.setEmail(userDetails.getEmail());
            user.setPassword(user.getPassword());

            BeanUser updated = userRepository.save(user);
            return ResponseEntity.ok(new APIResponse("Usuario actualizado", updated, false, HttpStatus.OK));
        } catch (Exception e) {
            logger.error("Error al actualizar usuario: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new APIResponse("Error al actualizar usuario", true, HttpStatus.INTERNAL_SERVER_ERROR));
        }
    }


    // Activar/desactivar usuario
    @Transactional(rollbackFor = Exception.class)
    public ResponseEntity<APIResponse> toggleUserStatus(Long id) {
        try {
            BeanUser user = userRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

            user.setActive(!user.getActive());
            BeanUser updated = userRepository.save(user);
            return ResponseEntity.ok(new APIResponse("Estado del usuario actualizado", updated, false, HttpStatus.OK));
        } catch (Exception e) {
            logger.error("Error al cambiar estado del usuario: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new APIResponse("Error al cambiar estado", true, HttpStatus.INTERNAL_SERVER_ERROR));
        }
    }

    // Eliminar usuario
    @Transactional(rollbackFor = Exception.class)
    public ResponseEntity<APIResponse> deleteUser(Long id) {
        try {
            BeanUser user = userRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

            if (user.getStorage() != null) {
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body(new APIResponse("No se puede eliminar un usuario que tiene un almacén asignado", true, HttpStatus.CONFLICT));
            }

            userRepository.delete(user);
            return ResponseEntity.ok(new APIResponse("Usuario eliminado", false, HttpStatus.OK));
        } catch (Exception e) {
            logger.error("Error al eliminar usuario: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new APIResponse("Error al eliminar usuario", true, HttpStatus.INTERNAL_SERVER_ERROR));
        }
    }

    // Obtener usuarios por rol
    @Transactional(readOnly = true)
    public ResponseEntity<APIResponse> getUsersByRole() {
        try {
            List<BeanUser> users = userRepository.findByRolName("USER");
            return ResponseEntity.ok(new APIResponse("Usuarios por rol", users, false, HttpStatus.OK));
        } catch (Exception e) {
            logger.error("Error al obtener usuarios por rol: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new APIResponse("Error al obtener usuarios por rol", true, HttpStatus.INTERNAL_SERVER_ERROR));
        }
    }

    // Obtener responsables de almacén disponibles
    @Transactional(readOnly = true)
    public ResponseEntity<APIResponse> getAvailableWarehouseManagers() {
        try {
            List<BeanUser> users = userRepository.findByRolNameAndStorageIsNull("USER");
            return ResponseEntity.ok(new APIResponse("Responsables disponibles", users, false, HttpStatus.OK));
        } catch (Exception e) {
            logger.error("Error al obtener responsables disponibles: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new APIResponse("Error al obtener responsables disponibles", true, HttpStatus.INTERNAL_SERVER_ERROR));
        }
    }

    // Obtener usuarios activos
    @Transactional(readOnly = true)
    public ResponseEntity<APIResponse> getActiveUsers() {
        try {
            List<BeanUser> users = userRepository.findByActiveTrue();
            return ResponseEntity.ok(new APIResponse("Usuarios activos", users, false, HttpStatus.OK));
        } catch (Exception e) {
            logger.error("Error al obtener usuarios activos: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new APIResponse("Error al obtener usuarios activos", true, HttpStatus.INTERNAL_SERVER_ERROR));
        }
    }
    // Dentro de UserService

    @Transactional(rollbackFor = Exception.class)
    public ResponseEntity<APIResponse> changePassword(Long userId, String currentPassword, String newPassword) {
        try {
            Optional<BeanUser> optionalUser = userRepository.findById(userId);
            if (optionalUser.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new APIResponse("Usuario no encontrado", true, HttpStatus.NOT_FOUND));
            }
            BeanUser user = optionalUser.get();

            if (!passwordEncoder.matches(currentPassword, user.getPassword())) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(new APIResponse("La contraseña actual no es correcta", true, HttpStatus.UNAUTHORIZED));
            }

            user.setTemporal_password(false);
            user.setPassword(passwordEncoder.encode(newPassword));
            userRepository.saveAndFlush(user);
            return ResponseEntity.ok(new APIResponse("Contraseña actualizada exitosamente", false, HttpStatus.OK));
        } catch (Exception e) {
            logger.error("Error al cambiar la contraseña: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new APIResponse("Error al cambiar la contraseña", true, HttpStatus.INTERNAL_SERVER_ERROR));
        }
    }

}
