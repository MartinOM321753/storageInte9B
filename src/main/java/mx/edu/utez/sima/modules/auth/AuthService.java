package mx.edu.utez.sima.modules.auth;

import mx.edu.utez.sima.modules.Email.Emails;
import mx.edu.utez.sima.modules.auth.dto.LoginRequestDTO;
import mx.edu.utez.sima.modules.user.BeanUser;
import mx.edu.utez.sima.modules.user.UserRepository;
import mx.edu.utez.sima.security.jwt.JWTUtils;
import mx.edu.utez.sima.security.jwt.UDService;
import mx.edu.utez.sima.services.EmailService;
import mx.edu.utez.sima.utils.APIResponse;
import mx.edu.utez.sima.utils.GenerateCode;
import mx.edu.utez.sima.utils.PasswordEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;
import java.util.Optional;

@Service
public class AuthService {
    private final UserRepository userRepository;

    private final UDService udService;

    private final JWTUtils jwtUtils;

    private final EmailService emailService;

    private static final Logger log = LoggerFactory.getLogger(AuthService.class);

    public AuthService(UserRepository userRepository, UDService udService, JWTUtils jwtUtils, EmailService emailService) {
        this.userRepository = userRepository;
        this.udService = udService;
        this.jwtUtils = jwtUtils;
        this.emailService = emailService;
    }

    @Transactional(readOnly = true)
    public APIResponse doLogin(LoginRequestDTO payload){
        try {
            BeanUser found = userRepository.findByUsername(payload.getUsername())
                    .orElse(null);

            if (found == null){
                return new APIResponse(
                        "Usuario o contraseña incorrectos",
                        true,
                        HttpStatus.NOT_FOUND
                );
            }

            if (!PasswordEncoder.verifyPassword(payload.getPassword(), found.getPassword())){
                return new APIResponse(
                        "Usuario o contraseña incorrectos",
                        true,
                        HttpStatus.NOT_FOUND
                );
            }

            // Aquí usas el método que genera token con tu BeanUser completo
            String token = jwtUtils.generateToken(found);

            if (found.getTemporal_password() == true){
                return new APIResponse(
                        "Inicio de sesión exitoso",
                        token,
                        false,
                        HttpStatus.OK
                );
            }

            return new APIResponse(
                    "Inicio de sesión exitoso",
                    token,
                    false,
                    HttpStatus.OK
            );

        } catch (Exception ex){
            log.error("Error al iniciar sesion", ex);
            return new APIResponse(
                    "Error al iniciar sesion",
                    true,
                    HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }

    @Transactional(rollbackFor = {SQLException.class, Exception.class, Exception.class})
    public APIResponse register(BeanUser payload) {
        try {
          BeanUser found = userRepository.findByUsername(payload.getUsername())
                  .orElse(null);

            if (found != null) {
                return new APIResponse(
                        "El usuario ya existe",
                        true,
                        HttpStatus.CONFLICT
                );
            }

            payload.setPassword(PasswordEncoder.encodePassword(payload.getPassword()));
            BeanUser saved = userRepository.save(payload);
            return new APIResponse(
                    "Usuario registrado exitosamente",
                    saved,
                    false,
                    HttpStatus.CREATED
            );
        } catch (Exception ex) {
            log.error("Error al registrar el usuario", ex);
            return new APIResponse(
                    "Error al registrar el usuario",
                    true,
                    HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public APIResponse forwardPassword(String email) {
        try {
            Optional<BeanUser> user = userRepository.findByEmail(email);

            if (user.isEmpty()) {
                return new  APIResponse("El usuario no existe", true,HttpStatus.NOT_FOUND);
            }
            if (!user.get().getActive()) {
                return  new  APIResponse("Cuenta suspendida", true , HttpStatus.BAD_REQUEST );
            }


            String newPassword = GenerateCode.generatePassword();
            user.get().setPassword(PasswordEncoder.encodePassword(newPassword));
            user.get().setTemporal_password(true);

            userRepository.saveAndFlush(user.get());

            Emails emails = new Emails(user.get().getEmail(), newPassword, "Restauracion de contraseña");
            emailService.sendEmail(emails, 1);

            return new  APIResponse("Correo enviado correctamente",true, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Error al enviar el correo de restauración de contraseña", e);
            return new APIResponse( "Error desconocido: " , true, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
