package com.alerts.decorators;

public class BasicAlert implements AlertInterface{
    private String message;

    public BasicAlert(String message) {
        this.message = message;
    }

    @Override
    public String alertMessage() {
        return message;
    }
}
