package com.example.demo.controllers;

import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.ItemRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.ModifyCartRequest;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.Optional;

import static com.example.demo.controllers.ControllerECommerceUtils.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CartControllerTest {

    private CartController cartController;

    private UserRepository userRepository;
    private CartRepository cartRepository;
    private ItemRepository itemRepository;

    private User user;
    private Item item;
    private Cart cart;
    private ModifyCartRequest request;

    @Before
    public void Setup() {
        userRepository = mock(UserRepository.class);
        cartRepository = mock(CartRepository.class);
        itemRepository = mock(ItemRepository.class);

        cartController = new CartController(userRepository, cartRepository, itemRepository);

            item = getDefaultItem();
            user = getDefaultUser();
            cart = getDefaultCart();

        cart.setUser(user);
        user.setCart(cart);

        request = new ModifyCartRequest();
        request.setItemId(1L);
        request.setUsername(DEFAULT_USER_NAME_TEST);
        request.setQuantity(1);

        when(userRepository.findByUsername(DEFAULT_USER_NAME_TEST)).thenReturn(user);
        when(itemRepository.findById(1L)).thenReturn(Optional.of(item));

    }

    @Test
    public void addToCartWithSuccess() {
        final ResponseEntity<Cart> responseEntity = cartController.addTocart(request);
        final Cart responseCart = responseEntity.getBody();

        assertNotNull(responseEntity);
        assertEquals(200, responseEntity.getStatusCodeValue());
        assertNotNull(responseCart);
        assertEquals(2, responseCart.getItems().size());
        assertEquals(BigDecimal.valueOf(2.22), responseCart.getTotal());
        assertEquals(item, responseCart.getItems().get(1));
        assertEquals(user, responseCart.getUser());
    }

    @Test
    public void addToCartOfUserNotExistent() {
        request.setUsername("another-name");

        final ResponseEntity<Cart> responseEntity = cartController.addTocart(request);

        assertNotNull(responseEntity);
        assertNotEquals(200, responseEntity.getStatusCodeValue());
    }

    @Test
    public void addItemNotExistent() {
        request.setItemId(2L);

        final ResponseEntity<Cart> responseEntity = cartController.addTocart(request);

        assertNotNull(responseEntity);
        assertNotEquals(200, responseEntity.getStatusCodeValue());
    }

    @Test
    public void removeFromCartWithSuccess() {
        final ResponseEntity<Cart> responseEntity = cartController.removeFromcart(request);
        final Cart cart = responseEntity.getBody();

        assertNotNull(responseEntity);
        assertEquals(200, responseEntity.getStatusCodeValue());
        assertNotNull(cart);
        assertEquals(0, cart.getItems().size());
        assertEquals(user, cart.getUser());
    }

    @Test
    public void removeFromCartOfUserNotExistent() {
        request.setUsername("another-name");

        final ResponseEntity<Cart> responseEntity = cartController.removeFromcart(request);

        assertNotNull(responseEntity);
        assertNotEquals(200, responseEntity.getStatusCodeValue());
    }

    @Test
    public void removeFromCartItemNotExistent() {
        request.setItemId(2L);

        final ResponseEntity<Cart> responseEntity = cartController.removeFromcart(request);

        assertNotNull(responseEntity);
        assertNotEquals(200, responseEntity.getStatusCodeValue());
    }

}