package ec.com.sofka.data;

import java.io.Serializable;

public class AuthResponseDTO implements Serializable {
    private String token;

    public AuthResponseDTO(String token) {
        this.token = token;
    }

    public AuthResponseDTO() {
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

}