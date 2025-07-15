package utez.edu.mx.storage.modules.user;

import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonIgnore;

import utez.edu.mx.storage.modules.rol.Rol;
import utez.edu.mx.storage.modules.storage.Storage;

import java.time.LocalDateTime;
@Entity
@Table(name = "user")
public class BeanUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "uuid", nullable = false, unique = true)
    private String uuid;

    @Column(name = "username", nullable = false, unique = true, length = 50)
    private String username;

    @Column(name = "password", nullable = false, length = 255)
    private String password;

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Column(name = "last_name", nullable = false, length = 100)
    private String lastName;

    @Column(name = "email", nullable = false, unique = true, length = 100)
    private String email;

    @Column(name = "active", nullable = false)
    private Boolean active = true;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "role_id", nullable = false)
    private Rol rol;

    @OneToOne(mappedBy = "responsible")
    @JsonIgnore
    private Storage storage;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
    public BeanUser() {
    }

    public BeanUser(Long id, String uuid, String username, String password, String name,String lastName, String email, Boolean active, LocalDateTime createdAt, Rol rol, Storage storage) {
        this.id = id;
        this.uuid = uuid;
        this.username = username;
        this.password = password;
        this.name = name;
        this.lastName = lastName;
        this.email = email;
        this.active = active;
        this.createdAt = createdAt;
        this.rol = rol;
        this.storage = storage;
    }


    public Storage getStorage() {
        return storage;
    }

    public void setStorage(Storage storage) {
        this.storage = storage;
    }

    public Rol getRol() {
        return rol;
    }

    public void setRol(Rol role) {
        this.rol = role;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}