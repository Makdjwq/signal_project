package com.alerts.decorators;

/**
 * Class for priority alerts, adds 'Priority' to the message
 */
public class PriorityAlertDecorator extends AlertDecorator {
    public PriorityAlertDecorator(AlertInterface decoratedAlert) {
        super(decoratedAlert);
    }

    @Override
    public String alertMessage() {
        return "Priority: " + super.alertMessage();
    }
}
