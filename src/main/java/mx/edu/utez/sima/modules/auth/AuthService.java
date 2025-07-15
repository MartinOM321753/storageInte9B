package mx.edu.utez.sima.modules.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import mx.edu.utez.sima.modules.auth.dto.LoginRequestDTO;
import mx.edu.utez.sima.modules.user.BeanUser;
import mx.edu.utez.sima.modules.user.UserRpository;
import mx.edu.utez.sima.security.jwt.JWTUtils;
import mx.edu.utez.sima.security.jwt.UDService;
import mx.edu.utez.sima.utils.APIResponse;
import mx.edu.utez.sima.utils.PasswordEncoder;

import java.sql.SQLException;

@Service
public class AuthService {
    @Autowired
    private UserRpository userRpository;

    @Autowired
    private UDService udService;

    @Autowired
    private JWTUtils jwtUtils;

    @Transactional(readOnly = true)
    public APIResponse doLogin(LoginRequestDTO payload){


        try {
            BeanUser found = userRpository.findByUsername(payload.getUsername())
                    .orElse(null);

            if (!PasswordEncoder.verifyPassword(payload.getPassword(), found.getPassword())){
                return new APIResponse(
                        "Usuario o contraseña incorrectos",
                        true,
                        HttpStatus.NOT_FOUND
                );
            }
            if (found == null){
                return new APIResponse(
                        "Usuario o contraseña incorrectos",
                        true,
                        HttpStatus.NOT_FOUND
                );
            }

            UserDetails ud = udService.loadUserByUsername(found.getUsername());
            String token = jwtUtils.generateToken(ud);
            return new APIResponse(
                    "Inicio de sesión exitoso",
                    token,
                    false,
                    HttpStatus.OK
            );

        } catch (Exception ex){
            ex.printStackTrace();
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
          BeanUser found = userRpository.findByUsername(payload.getUsername())
                  .orElse(null);

            if (found != null) {
                return new APIResponse(
                        "El usuario ya existe",
                        true,
                        HttpStatus.CONFLICT
                );
            }

            payload.setPassword(PasswordEncoder.encodePassword(payload.getPassword()));
            BeanUser saved = userRpository.save(payload);
            return new APIResponse(
                    "Usuario registrado exitosamente",
                    saved,
                    false,
                    HttpStatus.CREATED
            );
        } catch (Exception ex) {
            ex.printStackTrace();
            return new APIResponse(
                    "Error al registrar el usuario",
                    true,
                    HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }
}
