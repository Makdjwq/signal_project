package data_management;
import com.alerts.decorators.AlertInterface;
import com.alerts.decorators.BasicAlert;
import com.alerts.decorators.PriorityAlertDecorator;
import com.alerts.decorators.RepeatedAlertDecorator;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AlertDecoratorTest {
    @Test
    public void testBasicAlert() {
        AlertInterface alert = new BasicAlert("This is a basic alert.");
        assertEquals("This is a basic alert.", alert.alertMessage());
    }

    @Test
    public void testPriorityAlertDecorator() {
        AlertInterface alert = new BasicAlert("This is a basic alert.");
        AlertInterface priorityAlert = new PriorityAlertDecorator(alert);
        assertEquals("Priority: This is a basic alert.", priorityAlert.alertMessage());
    }

    @Test
    public void testRepeatedAlertDecorator() {
        AlertInterface alert = new BasicAlert("This is a basic alert.");
        AlertInterface repeatedAlert = new RepeatedAlertDecorator(alert);
        assertEquals("This is a basic alert. (Repeated Alert)", repeatedAlert.alertMessage());
    }

    @Test
    public void testPriorityAndRepeatedAlertDecorator() {
        AlertInterface alert = new BasicAlert("This is a basic alert.");
        AlertInterface priorityAlert = new PriorityAlertDecorator(alert);
        AlertInterface repeatedPriorityAlert = new RepeatedAlertDecorator(priorityAlert);
        assertEquals("Priority: This is a basic alert. (Repeated Alert)", repeatedPriorityAlert.alertMessage());
    }

    @Test
    public void testRepeatedAndPriorityAlertDecorator() {
        AlertInterface alert = new BasicAlert("This is a basic alert.");
        AlertInterface repeatedAlert = new RepeatedAlertDecorator(alert);
        AlertInterface priorityRepeatedAlert = new PriorityAlertDecorator(repeatedAlert);
        assertEquals("Priority: This is a basic alert. (Repeated Alert)", priorityRepeatedAlert.alertMessage());
    }
}
