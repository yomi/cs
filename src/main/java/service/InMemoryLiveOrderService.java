package service;

import exception.OrderNotFound;
import model.BuySell;
import model.Order;
import model.OrderSummary;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Collectors;

public class InMemoryLiveOrderService extends AbstractLiveOrderService implements LiveOrderService {

    private ConcurrentMap<String, Order> orders = new ConcurrentHashMap<>();

    @Override
    protected List<Order> loadOrders(BuySell buySell) {
        return orders.values().stream()
                .filter(order -> buySell == order.getBuySell())
                .collect(Collectors.toList());
    }

    @Override
    protected Order removeOrder(String orderRef) {
        return orders.remove(orderRef);
    }

    @Override
    protected Order loadOrder(String orderRef) {
        return orders.get(orderRef);
    }

    @Override
    protected Order saveOrder(Order order) {
        orders.put(order.getOrderRef(), order);
        return order;
    }
}
