package com.alerts.decorators;

public abstract class AlertDecorator implements AlertInterface {
    protected AlertInterface decoratedAlert;

    public AlertDecorator(AlertInterface decoratedAlert) {
        this.decoratedAlert = decoratedAlert;
    }

    @Override
    public String alertMessage() {
        return decoratedAlert.alertMessage();
    }
}
