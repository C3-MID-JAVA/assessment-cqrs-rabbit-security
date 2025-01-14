package ec.com.sofka;

public class AdminDTO {
    private String id;
    private String email;
    private String password;
    private ROLE role;

    public AdminDTO(String id, String email, String password, ROLE role) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.role = role;
    }

    public AdminDTO(String email, String password, ROLE role) {
        this.email = email;
        this.password = password;
        this.role = role;
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ROLE getRole() {
        return role;
    }

    public void setRole(ROLE role) {
        this.role = role;
    }
}
