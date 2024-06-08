package com.alerts.decorators;

/**
 * Abstract class for decorators
 */
public abstract class AlertDecorator implements AlertInterface {
    protected AlertInterface decoratedAlert;

    public AlertDecorator(AlertInterface decoratedAlert) {
        this.decoratedAlert = decoratedAlert;
    }

    /**
     * Method used to make a 'decorated' alert
     * @return a String with the message
     */
    @Override
    public String alertMessage() {
        return decoratedAlert.alertMessage();
    }
}
