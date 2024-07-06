package it.epicode.backend.capstone.errors;

import lombok.Data;

@Data
public class GeneralResponse<T> {
    private T data;
    private String errorMessage;

    public GeneralResponse(T data) {
        this.data = data;
        this.errorMessage = null;
    }

    public GeneralResponse(String errorMessage) {
        this.data = null;
        this.errorMessage = errorMessage;
    }
}
