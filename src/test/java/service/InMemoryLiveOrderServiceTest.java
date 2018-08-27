package service;

import model.Order;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static model.BuySell.BUY;
import static model.BuySell.SELL;
import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

public class InMemoryLiveOrderServiceTest extends LiveOrderServiceTest {

    @Override
    protected LiveOrderService createOrderService() {
        return new InMemoryLiveOrderService();
    }

    @Test
    public void loadOrders_empty_buy() {
        InMemoryLiveOrderService inMemoryLiveOrderService = getInMemoryLiveOrderService();

        List<Order> orders = inMemoryLiveOrderService.loadOrders(BUY);
        assertThat(orders, notNullValue());
        assertThat(orders.size(), is(0));
    }

    @Test
    public void loadOrders_empty_sell() {
        InMemoryLiveOrderService inMemoryLiveOrderService = getInMemoryLiveOrderService();

        List<Order> orders = inMemoryLiveOrderService.loadOrders(SELL);
        assertThat(orders, notNullValue());
        assertThat(orders.size(), is(0));
    }

    @Test
    public void loadOrders_Buy() {
        InMemoryLiveOrderService inMemoryLiveOrderService = getInMemoryLiveOrderService();

        Order order1 = inMemoryLiveOrderService.registerOrder("user1", 3.5, 306, BUY);
        assertThat(order1.getOrderRef(), notNullValue());
        Order order2 = inMemoryLiveOrderService.registerOrder("user3", 1.5, 307, BUY);
        assertThat(order2.getOrderRef(), notNullValue());
        Order order3 = inMemoryLiveOrderService.registerOrder("user4", 2.0, 306, BUY);
        assertThat(order3.getOrderRef(), notNullValue());
        Order order4 = inMemoryLiveOrderService.registerOrder("user3", 1.5, 309, SELL);
        assertThat(order4.getOrderRef(), notNullValue());
        Order order5 = inMemoryLiveOrderService.registerOrder("user4", 2.0, 409, SELL);
        assertThat(order5.getOrderRef(), notNullValue());
        Order order6 = inMemoryLiveOrderService.registerOrder("user2", 1.2, 310, BUY);
        assertThat(order6.getOrderRef(), notNullValue());

        List<Order> orders = inMemoryLiveOrderService.loadOrders(BUY);
        assertThat(orders, notNullValue());
        assertThat(orders.size(), is(4));

        assertThat(orders, hasItem(order1));
        assertThat(orders, hasItem(order2));
        assertThat(orders, hasItem(order3));
        assertThat(orders, hasItem(order6));
    }

    @Test
    public void loadOrders_Sell() {
        InMemoryLiveOrderService inMemoryLiveOrderService = getInMemoryLiveOrderService();

        Order order1 = inMemoryLiveOrderService.registerOrder("user1", 3.5, 306, BUY);
        assertThat(order1.getOrderRef(), notNullValue());
        Order order2 = inMemoryLiveOrderService.registerOrder("user3", 1.5, 307, BUY);
        assertThat(order2.getOrderRef(), notNullValue());
        Order order3 = inMemoryLiveOrderService.registerOrder("user4", 2.0, 306, BUY);
        assertThat(order3.getOrderRef(), notNullValue());
        Order order4 = inMemoryLiveOrderService.registerOrder("user3", 1.5, 309, SELL);
        assertThat(order4.getOrderRef(), notNullValue());
        Order order5 = inMemoryLiveOrderService.registerOrder("user4", 2.0, 409, SELL);
        assertThat(order5.getOrderRef(), notNullValue());
        Order order6 = inMemoryLiveOrderService.registerOrder("user2", 1.2, 310, BUY);
        assertThat(order6.getOrderRef(), notNullValue());

        List<Order> orders = inMemoryLiveOrderService.loadOrders(SELL);
        assertThat(orders, notNullValue());
        assertThat(orders.size(), is(2));

        assertThat(orders, hasItem(order4));
        assertThat(orders, hasItem(order5));
    }

    protected InMemoryLiveOrderService getInMemoryLiveOrderService() {
        return (InMemoryLiveOrderService)getLiveOrderService();
    }
}