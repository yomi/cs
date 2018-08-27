package model;

import validate.InputValidator;

import java.util.Objects;

public class OrderSummary {
    private final double quantity;
    private final int price;

    public OrderSummary(double quantity, int price) {
        InputValidator.validateQuantity(quantity);
        InputValidator.validatePrice(price);

        this.quantity = quantity;
        this.price = price;
    }

    public double getQuantity() {
        return quantity;
    }

    public int getPrice() {
        return price;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderSummary that = (OrderSummary) o;
        return Double.compare(that.getQuantity(), getQuantity()) == 0 &&
                getPrice() == that.getPrice();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getQuantity(), getPrice());
    }

    @Override
    public String toString() {
        return String.format("%s kg for £%s", quantity, price);
    }
}
