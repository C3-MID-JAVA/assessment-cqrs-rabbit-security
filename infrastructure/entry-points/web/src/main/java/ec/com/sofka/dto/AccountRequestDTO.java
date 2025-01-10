package ec.com.sofka.dto;

import jakarta.validation.constraints.NotBlank;

public class AccountRequestDTO {

    @NotBlank(message = "UserId cannot be blank")
    private String aggregateId;

    public AccountRequestDTO() {
    }

    public AccountRequestDTO(String aggregateId) {
        this.aggregateId = aggregateId;
    }

    public String getAggregateId() {
        return aggregateId;
    }

    public void setAggregateId(String aggregateId) {
        this.aggregateId = aggregateId;
    }
}
