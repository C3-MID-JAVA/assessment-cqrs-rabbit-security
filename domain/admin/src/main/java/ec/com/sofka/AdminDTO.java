package ec.com.sofka;

public class AdminDTO {
    private String id;
    private String email;
    private String password;
    private RoleEnum roleEnum;

    public AdminDTO(String id, String email, String password, RoleEnum roleEnum) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.roleEnum = roleEnum;
    }

    public AdminDTO(String email, String password, RoleEnum roleEnum) {
        this.email = email;
        this.password = password;
        this.roleEnum = roleEnum;
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

    public RoleEnum getRole() {
        return roleEnum;
    }

    public void setRole(RoleEnum roleEnum) {
        this.roleEnum = roleEnum;
    }
}