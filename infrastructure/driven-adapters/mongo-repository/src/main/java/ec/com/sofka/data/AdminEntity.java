package ec.com.sofka.data;

import ec.com.sofka.RoleEnum;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "admins")
public class AdminEntity {

    @Id
    private String id;

    private String email;

    private String password;

    private RoleEnum role;

    public AdminEntity() {
    }

    public AdminEntity(String email, String password, RoleEnum role) {
        this.email = email;
        this.password = password;
        this.role = role;
    }

    public AdminEntity(String id, String email, String password, RoleEnum role) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.role = role;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public RoleEnum getRole() {
        return role;
    }

    public void setRole(RoleEnum role) {
        this.role = role;
    }
}
