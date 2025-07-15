package utez.edu.mx.storage.modules.rol;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import utez.edu.mx.storage.modules.user.BeanUser;

import java.util.List;

@Entity
@Table(name = "rol")
public class Rol {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @OneToMany(mappedBy = "rol")
    @JsonIgnore
    private List<BeanUser> user;

    public Rol() {
    }

    public Rol(Long id, String name, List<BeanUser> user) {
        this.id = id;
        this.name = name;
        this.user = user;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<BeanUser> getUser() {
        return user;
    }

    public void setUser(List<BeanUser> user) {
        this.user = user;
    }
}
