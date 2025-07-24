package mx.edu.utez.sima.modules.user;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public class UserDTO {
    private Long id;
    private String username;

    private String password;
    @Pattern(regexp = "^[a-zA-ZáéíóúÁÉÍÓÚÑñ][a-zA-ZáéíóúÁÉÍÓÚÑñ\\s]{3,}$", message = "Solamente se aceptan letras")
    @NotNull(message = "Favor de ingresar los datos")
    @NotBlank(message = "Favor de no dejar los datos en blanco")
    private String name;
    @Pattern(regexp = "^[a-zA-ZáéíóúÁÉÍÓÚÑñ][a-zA-ZáéíóúÁÉÍÓÚÑñ\\s]{3,}$", message = "Solamente se aceptan letras")
    @NotNull(message = "Favor de ingresar los datos")
    @NotBlank(message = "Favor de no dejar los datos en blanco")
    private String lastName;
    @Pattern(regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$", message = "Ingrensa un correo valido")
    @NotNull(message = "Favor de ingresar el correo")
    @NotBlank(message = "Favor de no dejar los datos en blanco")
    @Email
    private String email;




    public BeanUser toEntity() {
        BeanUser user = new BeanUser();

        user.setUsername(this.username);
        user.setPassword(this.password);
        user.setName(this.name);
        user.setLastName(this.lastName);
        user.setEmail(this.email);
        user.setActive(true);
        user.setTemporal_password(false);

        return user;
    }

    public BeanUser toEntityUpdate() {
        BeanUser user = new BeanUser();
        user.setId(this.id);
        user.setUsername(this.username);
        user.setPassword(this.password);
        user.setName(this.name);
        user.setLastName(this.lastName);
        user.setEmail(this.email);
        user.setActive(true);
        user.setTemporal_password(false);

        return user;
    }

    public UserDTO(Long id, String username, String password, String name, String lastName, String email) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.name = name;
        this.lastName = lastName;
        this.email = email;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


}
