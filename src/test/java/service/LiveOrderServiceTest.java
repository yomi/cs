package service;

import exception.IllegalPrice;
import exception.IllegalQuantity;
import exception.IllegalUserId;
import exception.OrderNotFound;
import model.Order;
import model.OrderSummary;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.List;
import java.util.stream.Collectors;

import static java.lang.Double.compare;
import static model.BuySell.BUY;
import static model.BuySell.SELL;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

public abstract class LiveOrderServiceTest {

    @Rule
    public ExpectedException exception = ExpectedException.none();

    private LiveOrderService liveOrderService;

    @Before
    public void setUp() {
        liveOrderService = createOrderService();
    }

    protected abstract LiveOrderService createOrderService();

    @Test
    public void registerOrder_withNullBuySell() {
        exception.expect(NullPointerException.class);
        exception.expectMessage(containsString("BuySell must not be blank"));
        liveOrderService.registerOrder(null, 10.0, 300, null);
    }

    @Test
    public void registerOrder_withEmptyUserId() {
        exception.expect(IllegalUserId.class);
        exception.expectMessage(containsString("UserId="));
        liveOrderService.registerOrder("", 10.0, 300, BUY);
    }

    @Test
    public void registerOrder_withNullUserId() {
        exception.expect(IllegalUserId.class);
        exception.expectMessage(containsString("UserId=null"));
        liveOrderService.registerOrder(null, 10.0, 300, BUY);
    }

    @Test
    public void registerOrder_withNegativeQuantity() {
        exception.expect(IllegalQuantity.class);
        exception.expectMessage(containsString("Quantity=-10.0"));
        liveOrderService.registerOrder("User1", -10.0, 300, BUY);
    }

    @Test
    public void registerOrder_withZeroQuantity() {
        exception.expect(IllegalQuantity.class);
        exception.expectMessage(containsString("Quantity=0.0"));
        liveOrderService.registerOrder("User1", 0.0, 300, BUY);
    }

    @Test
    public void registerOrder_withNegativePrice() {
        exception.expect(IllegalPrice.class);
        exception.expectMessage(containsString("Price=-300"));
        liveOrderService.registerOrder("User1", 1.0, -300, BUY);
    }

    @Test
    public void registerOrder_withZeroPrice() {
        exception.expect(IllegalPrice.class);
        exception.expectMessage(containsString("Price=0"));
        liveOrderService.registerOrder("User1", 1.0, 0, BUY);
    }

    @Test
    public void registerOrder() {
        Order registeredOrder = liveOrderService.registerOrder("User1", 1.0, 300, BUY);
        assertThat(registeredOrder, notNullValue());
        assertThat(registeredOrder.getOrderRef(), notNullValue());
        assertThat(registeredOrder.getUserId(), is("User1"));
        assertThat(0, is(compare(registeredOrder.getQuantity(), 1.0)));
        assertThat(registeredOrder.getPrice(), is(300));
        assertThat(registeredOrder.getBuySell(), is(BUY));
    }

    @Test
    public void getOrder() {
        Order order = liveOrderService.registerOrder("User1", 1.0, 300, BUY);
        assertThat(order, notNullValue());
        assertThat(order.getOrderRef(), notNullValue());

        Order registeredOrder = liveOrderService.getOrder(order.getOrderRef());
        assertThat(registeredOrder, notNullValue());
        assertThat(registeredOrder.getOrderRef(), is(order.getOrderRef()));
        assertThat(registeredOrder.getUserId(), is("User1"));
        assertThat(0, is(compare(registeredOrder.getQuantity(), 1.0)));
        assertThat(registeredOrder.getPrice(), is(300));
        assertThat(registeredOrder.getBuySell(), is(BUY));
    }

    @Test
    public void getOrder_UnknownOrderRef() {
        exception.expect(OrderNotFound.class);
        exception.expectMessage(containsString("OrderRef=UnknownOrderRef"));

        liveOrderService.getOrder("UnknownOrderRef");
    }


    @Test
    public void cancelOrder() {
        Order order = liveOrderService.registerOrder("User1", 1.0, 300, BUY);
        assertThat(order, notNullValue());
        assertThat(order.getOrderRef(), notNullValue());

        Order registeredOrder = liveOrderService.getOrder(order.getOrderRef());
        assertThat(registeredOrder, notNullValue());
        assertThat(registeredOrder.getOrderRef(), is(order.getOrderRef()));
        assertThat(registeredOrder.getUserId(), is("User1"));
        assertThat(0, is(compare(registeredOrder.getQuantity(), 1.0)));
        assertThat(registeredOrder.getPrice(), is(300));
        assertThat(registeredOrder.getBuySell(), is(BUY));

        liveOrderService.cancelOrder(order.getOrderRef());
        exception.expect(OrderNotFound.class);
        exception.expectMessage(containsString("OrderRef=" + order.getOrderRef()));
        liveOrderService.getOrder(order.getOrderRef());
    }

    @Test
    public void cancelOrder_notIn_summary() {
        Order order1 = liveOrderService.registerOrder("User1", 1.0, 300, BUY);
        assertThat(order1, notNullValue());
        assertThat(order1.getOrderRef(), notNullValue());
        Order order2 = liveOrderService.registerOrder("user3", 1.5, 307, BUY);
        assertThat(order2, notNullValue());
        assertThat(order2.getOrderRef(), notNullValue());

        List<OrderSummary> orderSummaries = liveOrderService.buySummary();
        assertThat(orderSummaries, notNullValue());
        assertThat(orderSummaries.size(), is(2));
        List<String> orderSummariesDesc = orderSummaries.stream()
                .map(orderSummary -> orderSummary.toString())
                .collect(Collectors.toList());

        assertThat(orderSummariesDesc, notNullValue());
        assertThat(orderSummariesDesc.size(), is(2));

        assertThat("1.5 kg for £307", is(orderSummariesDesc.get(0)));
        assertThat("1.0 kg for £300", is(orderSummariesDesc.get(1)));


        liveOrderService.cancelOrder(order1.getOrderRef());
        orderSummaries = liveOrderService.buySummary();
        assertThat(orderSummaries, notNullValue());
        assertThat(orderSummaries.size(), is(1));
        orderSummariesDesc = orderSummaries.stream()
                .map(orderSummary -> orderSummary.toString())
                .collect(Collectors.toList());

        assertThat(orderSummariesDesc, notNullValue());
        assertThat(orderSummariesDesc.size(), is(1));

        assertThat("1.5 kg for £307", is(orderSummariesDesc.get(0)));
    }

    @Test
    public void cancelOrder_UnknownOrderRef() {
        exception.expect(OrderNotFound.class);
        exception.expectMessage(containsString("OrderRef=UnknownOrderRef"));

        liveOrderService.cancelOrder("UnknownOrderRef");
    }

    @Test
    public void buySummary_emptyOrders() {
        List<OrderSummary> orderSummaries = liveOrderService.buySummary();
        assertThat(orderSummaries, notNullValue());
        assertThat(orderSummaries.size(), is(0));
    }

    @Test
    public void buySummary() {
        Order order = liveOrderService.registerOrder("user1", 3.5, 306, BUY);
        assertThat(order.getOrderRef(), notNullValue());
        order = liveOrderService.registerOrder("user3", 1.5, 307, BUY);
        assertThat(order.getOrderRef(), notNullValue());
        order = liveOrderService.registerOrder("user4", 2.0, 306, BUY);
        assertThat(order.getOrderRef(), notNullValue());
        order = liveOrderService.registerOrder("user3", 1.5, 309, SELL);
        assertThat(order.getOrderRef(), notNullValue());
        order = liveOrderService.registerOrder("user4", 2.0, 409, SELL);
        assertThat(order.getOrderRef(), notNullValue());
        order = liveOrderService.registerOrder("user2", 1.2, 310, BUY);
        assertThat(order.getOrderRef(), notNullValue());

        List<OrderSummary> orderSummaries = liveOrderService.buySummary();
        assertThat(orderSummaries, notNullValue());
        assertThat(orderSummaries.size(), is(3));

        List<String> orderSummariesDesc = orderSummaries.stream()
                .map(orderSummary -> orderSummary.toString())
                .collect(Collectors.toList());

        assertThat(orderSummariesDesc, notNullValue());
        assertThat(orderSummariesDesc.size(), is(3));

        assertThat("1.2 kg for £310", is(orderSummariesDesc.get(0)));
        assertThat("1.5 kg for £307", is(orderSummariesDesc.get(1)));
        assertThat("5.5 kg for £306", is(orderSummariesDesc.get(2)));
    }

    @Test
    public void sellSummary_emptyOrders() {
        List<OrderSummary> orderSummaries = liveOrderService.sellSummary();
        assertThat(orderSummaries, notNullValue());
        assertThat(orderSummaries.size(), is(0));
    }

    @Test
    public void sellSummary() {
        Order order = liveOrderService.registerOrder("user1", 3.5, 306, SELL);
        assertThat(order.getOrderRef(), notNullValue());
        order = liveOrderService.registerOrder("user3", 1.5, 307, SELL);
        assertThat(order.getOrderRef(), notNullValue());
        order = liveOrderService.registerOrder("user4", 2.0, 306, SELL);
        assertThat(order.getOrderRef(), notNullValue());
        order = liveOrderService.registerOrder("user3", 1.5, 309, BUY);
        assertThat(order.getOrderRef(), notNullValue());
        order = liveOrderService.registerOrder("user4", 2.0, 409, BUY);
        assertThat(order.getOrderRef(), notNullValue());
        order = liveOrderService.registerOrder("user2", 1.2, 310, SELL);
        assertThat(order.getOrderRef(), notNullValue());

        List<OrderSummary> orderSummaries = liveOrderService.sellSummary();
        assertThat(orderSummaries, notNullValue());
        assertThat(orderSummaries.size(), is(3));

        List<String> orderSummariesDesc = orderSummaries.stream()
                .map(orderSummary -> orderSummary.toString())
                .collect(Collectors.toList());

        assertThat(orderSummariesDesc, notNullValue());
        assertThat(orderSummariesDesc.size(), is(3));

        assertThat("5.5 kg for £306", is(orderSummariesDesc.get(0)));
        assertThat("1.5 kg for £307", is(orderSummariesDesc.get(1)));
        assertThat("1.2 kg for £310", is(orderSummariesDesc.get(2)));
    }

    public LiveOrderService getLiveOrderService() {
        return liveOrderService;
    }
}