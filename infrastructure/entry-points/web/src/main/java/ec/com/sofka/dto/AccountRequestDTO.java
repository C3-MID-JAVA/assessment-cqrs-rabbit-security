package ec.com.sofka.dto;

import jakarta.validation.constraints.NotBlank;

public class AccountRequestDTO {

    @NotBlank(message = "UserId cannot be blank")
    private String userId;

    public AccountRequestDTO() {
    }

    public AccountRequestDTO(String userId) {
        this.userId = userId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
