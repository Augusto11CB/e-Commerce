package com.example.demo.controllers;

import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.UserOrder;
import com.example.demo.model.persistence.repositories.OrderRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static com.example.demo.controllers.ControllerECommerceUtils.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class OrderControllerTest {

    private OrderController orderController;
    private UserRepository userRepository;
    private OrderRepository orderRepository;

    private User user;
    private Item item;
    private Cart cart;

    @Before
    public void Setup() {

        userRepository = mock(UserRepository.class);
        orderRepository = mock(OrderRepository.class);
        orderController = new OrderController(userRepository, orderRepository);

        item = getDefaultItem();
        user = getDefaultUser();
        cart = getDefaultCart();

        cart.setUser(user);
        user.setCart(cart);

        when(userRepository.findByUsername(DEFAULT_USER_NAME_TEST)).thenReturn(user);

    }

    @Test
    public void submitOrderWithSuccess() {

        final ResponseEntity<UserOrder> responseEntity = orderController.submit(DEFAULT_USER_NAME_TEST);
        final UserOrder returnedOrder = responseEntity.getBody();

        assertNotNull(responseEntity);
        assertEquals(200, responseEntity.getStatusCodeValue());
        assertNotNull(returnedOrder);
        assertEquals(1, returnedOrder.getItems().size());
        assertEquals(DEFAULT_ITEM_NAME_TEST, returnedOrder.getItems().get(0).getName());
        assertEquals(1L, returnedOrder.getUser().getId());
        assertEquals(DEFAULT_ITEM_PRICE_TEST, returnedOrder.getTotal());
    }

    @Test
    public void submitOrderWithUserNotExistent() {
        final ResponseEntity<UserOrder> responseEntity = orderController.submit("bla");

        assertNotNull(responseEntity);
        assertNotEquals(200, responseEntity.getStatusCodeValue());
    }

    @Test
    public void getAllOrderHistoryForUser() {

        List<Item> items = new ArrayList<>();
        items.add(item);

        final UserOrder order = new UserOrder();
        order.setId(1L);
        order.setItems(items);
        order.setUser(user);
        order.setTotal(DEFAULT_ITEM_PRICE_TEST);

        final List<UserOrder> orders = new ArrayList<>();
        orders.add(order);

        when(orderRepository.findByUser(user)).thenReturn(orders);

        final ResponseEntity<List<UserOrder>> responseEntity = orderController.getOrdersForUser(DEFAULT_USER_NAME_TEST);
        final List<UserOrder> history = responseEntity.getBody();

        assertNotNull(responseEntity);
        assertEquals(200, responseEntity.getStatusCodeValue());

        assertNotNull(history);
        assertEquals(1, history.size());
        assertEquals(DEFAULT_ITEM_NAME_TEST, history.get(0).getItems().get(0).getName());
        assertEquals(DEFAULT_ITEM_PRICE_TEST, history.get(0).getTotal());
    }

    @Test
    public void historyUserNotFound() {
        final List<Item> items = new ArrayList<>();
        items.add(item);

        final UserOrder order = new UserOrder();
        order.setId(1L);
        order.setItems(items);
        order.setUser(user);
        order.setTotal(BigDecimal.valueOf(3.99));

        final List<UserOrder> orders = new ArrayList<>();
        orders.add(order);

        when(orderRepository.findByUser(user)).thenReturn(orders);

        final ResponseEntity<List<UserOrder>> responseEntity = orderController.getOrdersForUser("bla");

        assertNotNull(responseEntity);
        assertNotEquals(200, responseEntity.getStatusCodeValue());
    }
}