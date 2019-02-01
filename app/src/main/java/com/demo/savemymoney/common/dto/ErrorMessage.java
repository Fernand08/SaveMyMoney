package com.demo.savemymoney.common.dto;

public class ErrorMessage {
    private Integer inputId;
    private String message;

    public ErrorMessage(Integer inputId, String message) {
        this.inputId = inputId;
        this.message = message;
    }

    public Integer getInputId() {
        return inputId;
    }

    public void setInputId(Integer inputId) {
        this.inputId = inputId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
