package com.alerts.decorators;

/**
 * Basic alert with just a message
 */
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
