package service;

import exception.OrderNotFound;
import model.BuySell;
import model.Order;
import model.OrderSummary;

import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.groupingBy;
import static model.BuySell.*;

public abstract class AbstractLiveOrderService implements LiveOrderService {

    private static final Comparator<OrderSummary> BY_PRICE = Comparator.comparingInt(OrderSummary::getPrice);

    private static OrderSummary toOrderSummary(List<Order> orders) {
        double totalQuantity = orders.stream()
                .mapToDouble(Order::getQuantity)
                .sum();
        return new OrderSummary(totalQuantity, orders.stream().findFirst().get().getPrice());
    }

    @Override
    public Order registerOrder(String userId, double quantity, int price, BuySell buySell) {
        String uuid = UUID.randomUUID().toString();
        return saveOrder(new Order(uuid, userId, quantity, price, buySell));
    }

    @Override
    public Order getOrder(String orderRef) {
        Order order = loadOrder(orderRef);
        if (order == null) {
            throw new OrderNotFound(String.format("OrderRef=%s", orderRef));
        }
        return order;
    }

    @Override
    public Order cancelOrder(String orderRef) {
        Order order = getOrder(orderRef);
        return removeOrder(order.getOrderRef());
    }

    @Override
    public List<OrderSummary> buySummary() {
        Map<Integer, List<Order>> ordersByPrice = grpOrderByPrice(BUY);
        Set<OrderSummary> sellSummaries = ordersByPrice.values().stream()
                .map(AbstractLiveOrderService::toOrderSummary)
                .collect(Collectors.toCollection(() -> new TreeSet<>(BY_PRICE.reversed())));
        return new ArrayList<>(sellSummaries);
    }

    @Override
    public List<OrderSummary> sellSummary() {
        Map<Integer, List<Order>> ordersByPrice = grpOrderByPrice(SELL);
        Set<OrderSummary> sellSummaries = ordersByPrice.values().stream()
                .map(AbstractLiveOrderService::toOrderSummary)
                .collect(Collectors.toCollection(() -> new TreeSet<>(BY_PRICE)));
        return new ArrayList<>(sellSummaries);
    }

    protected Map<Integer, List<Order>> grpOrderByPrice(BuySell buySell) {
        List<Order> orders = loadOrders(buySell);
        return orders.stream().collect(groupingBy(Order::getPrice));
    }

    protected abstract List<Order> loadOrders(BuySell buySell);

    protected abstract Order removeOrder(String orderRef);

    protected abstract Order loadOrder(String orderRef);

    protected abstract Order saveOrder(Order order);
}
