package com.alerts.decorators;

public class RepeatedAlertDecorator extends AlertDecorator {
    public RepeatedAlertDecorator(AlertInterface decoratedAlert) {
        super(decoratedAlert);
    }

    @Override
    public String alertMessage() {
        return super.alertMessage() + " (Repeated Alert)";
    }
}
