package validate;

import exception.IllegalPrice;
import exception.IllegalQuantity;
import exception.IllegalUserId;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.hamcrest.CoreMatchers.containsString;
import static org.junit.Assert.*;
import static validate.InputValidator.validatePrice;
import static validate.InputValidator.validateQuantity;
import static validate.InputValidator.validateUserId;

public class InputValidatorTest {

    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Test
    public void validatePrice_positive() {
        validatePrice(10);
    }

    @Test
    public void validatePrice_zero() {
        exception.expect(IllegalPrice.class);
        exception.expectMessage(containsString("Price=0 not allowed"));

        validatePrice(0);
    }

    @Test
    public void validatePrice_negative() {
        exception.expect(IllegalPrice.class);
        exception.expectMessage(containsString("Price=-10 not allowed"));

        validatePrice(-10);
    }

    @Test
    public void validateQuantity_zero() {
        exception.expect(IllegalQuantity.class);
        exception.expectMessage(containsString("Quantity=0.0 not allowed"));

        validateQuantity(0.0);
    }

    @Test
    public void validateQuantity_negative() {
        exception.expect(IllegalQuantity.class);
        exception.expectMessage(containsString("Quantity=-10.0 not allowed"));

        validateQuantity(-10.0);
    }

    @Test
    public void validateQuantity_positive() {
        validateQuantity(10.0);
    }

    @Test
    public void validateUserId_blank() {
        exception.expect(IllegalUserId.class);
        exception.expectMessage(containsString("UserId= not allowed"));

        validateUserId("");
    }

    @Test
    public void validateUserId_null() {
        exception.expect(IllegalUserId.class);
        exception.expectMessage(containsString("UserId=null not allowed"));

        validateUserId(null);
    }
}