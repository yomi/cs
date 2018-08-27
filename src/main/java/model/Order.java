package model;

import java.util.Objects;

import static validate.InputValidator.validatePrice;
import static validate.InputValidator.validateQuantity;
import static validate.InputValidator.validateUserId;

/**
 * Represents an Order for a commodity
 */
public final class Order {
    /**
     * UUID for registered order
     */
    private final String orderRef;
    /**
     * The userId for user registering the order
     */
    private final String userId;
    /**
     * The order quantity, must be greater than zero
     */
    private final double quantity;
    /**
     * The order price must be greater than zero
     */
    private final int price;
    /**
     * Buy or Sell transaction type
     */
    private final BuySell buySell;

    public Order(String orderRef, String userId, double quantity, int price, BuySell buySell) {
        Objects.requireNonNull(orderRef, "OrderRef must not be blank");
        Objects.requireNonNull(buySell, "BuySell must not be blank");

        validateUserId(userId);
        validateQuantity(quantity);
        validatePrice(price);

        this.orderRef = orderRef;
        this.userId = userId;
        this.quantity = quantity;
        this.price = price;
        this.buySell = buySell;
    }

    public String getOrderRef() {
        return orderRef;
    }

    public String getUserId() {
        return userId;
    }

    public double getQuantity() {
        return quantity;
    }

    public int getPrice() {
        return price;
    }

    public BuySell getBuySell() {
        return buySell;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Order order = (Order) o;
        return Objects.equals(getOrderRef(), order.getOrderRef());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getOrderRef());
    }
}
