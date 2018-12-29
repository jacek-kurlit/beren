package pl.jlabs.beren.test;

import org.junit.jupiter.api.Test;
import pl.jlabs.beren.Beren;
import pl.jlabs.beren.test.model.OrdersCreateRequest;

class SimpleValidatorTest {
    @Test
    public void load() {
        SimpleValidator simpleValidator = Beren.loadValidator(SimpleValidator.class, 1, 1);

        OrdersCreateRequest request = new OrdersCreateRequest();
        request.setSource("Saruman,Gifts");
        request.setRequestId((short) 5);
        simpleValidator.validateRequest(request);

        System.out.println(simpleValidator);
    }
}