package com.alerts.decorators;

/**
 * Class for repeated alerts, adds information that the alert is repeated
 */
public class RepeatedAlertDecorator extends AlertDecorator {
    public RepeatedAlertDecorator(AlertInterface decoratedAlert) {
        super(decoratedAlert);
    }

    @Override
    public String alertMessage() {
        return super.alertMessage() + " (Repeated Alert)";
    }
}
