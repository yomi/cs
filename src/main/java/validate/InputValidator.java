package validate;

import exception.IllegalPrice;
import exception.IllegalQuantity;
import exception.IllegalUserId;

public final class InputValidator {

    public static void validatePrice(int price) {
        if (price <= 0) {
            throw new IllegalPrice(String.format("Price=%s not allowed", price));
        }
    }

    public static void validateQuantity(double quantity) {
        if (quantity <= 0) {
            throw new IllegalQuantity(String.format("Quantity=%s not allowed", quantity));
        }
    }

    public static void validateUserId(String userId) {
        if (userId == null || userId.isEmpty() || userId.startsWith(" ") || userId.endsWith(" ")) {
            throw new IllegalUserId(String.format("UserId=%s not allowed", userId));
        }
    }

    private InputValidator() {
    }
}
