package service;

import model.BuySell;
import model.Order;
import model.OrderSummary;

import java.util.List;

/**
 * LiveOrderService to manage orders and present order summaries by transaction type
 */
public interface LiveOrderService {

    /**
     * Register an Order
     *
     * @param userId the userId registering the order, must be provided
     * @param quantity the order quantity, must be provided
     * @param price the order price, must be provided
     * @param buySell the order transaction buy or sell, must be provided
     * @return the registered Order with OrderRef
     *
     * @throws exception.IllegalUserId if the userId is null or has illegal characters
     * @throws exception.IllegalQuantity if the quantity is 0 or negative
     * @throws exception.IllegalPrice if the price is 0 or negative
     */
    Order registerOrder(String userId, double quantity, int price, BuySell buySell);

    /**
     * Load an order from repo given an orderRef
     *
     * @param orderRef Order id provided when the order was registered
     * @return the registered order
     *
     * @throws exception.OrderNotFound if the orderRef is not found in the repositiory
     */
    Order getOrder(String orderRef);

    /**
     * Cancel a registered Order
     *
     * @param orderRef Order id provided when the order was registered
     * @return the canceled order
     *
     * @throws exception.OrderNotFound if the orderRef is not found in the repositiory
     */
    Order cancelOrder(String orderRef);

    /**
     * Return aggregated Buy order summary
     *
     * @return The list of Buy order summary
     */
    List<OrderSummary> buySummary();

    /**
     * Return aggregated Sell order summary
     *
     * @return The list of Sell order summary
     */
    List<OrderSummary> sellSummary();
}
