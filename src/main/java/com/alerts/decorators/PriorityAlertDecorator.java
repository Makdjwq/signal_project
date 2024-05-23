package com.alerts.decorators;

public class PriorityAlertDecorator extends AlertDecorator {
    public PriorityAlertDecorator(AlertInterface decoratedAlert) {
        super(decoratedAlert);
    }

    @Override
    public String alertMessage() {
        return "Priority: " + super.alertMessage();
    }
}
