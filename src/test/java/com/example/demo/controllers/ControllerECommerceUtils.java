package com.example.demo.controllers;

import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;

import java.math.BigDecimal;

public class ControllerECommerceUtils {

    public static String DEFAULT_USER_NAME_TEST = "Augusto-Test";
    public static String DEFAULT_USER_PASSWORD_TEST = "Augusto-Password";
    public static String DEFAULT_ITEM_NAME_TEST = "Item-Test";
    public static String DEFAULT_ITEM_DESCRIPTION_TEST = "test item";
    public static BigDecimal DEFAULT_ITEM_PRICE_TEST = BigDecimal.valueOf(1.11);

    public static User getDefaultUser() {
        User user = new User();
        user.setId(1L);
        user.setUsername(DEFAULT_USER_NAME_TEST);
        user.setPassword(DEFAULT_USER_PASSWORD_TEST);
        return user;
    }

    public static Item getDefaultItem() {
        Item item = new Item();
        item.setId(1L);
        item.setName(DEFAULT_ITEM_NAME_TEST);
        item.setPrice(DEFAULT_ITEM_PRICE_TEST);
        item.setDescription("test item");
        return item;
    }

    public static Cart getDefaultCart() {
        Cart cart = new Cart();
        cart.setId(1L);
        cart.addItem(getDefaultItem());
        return cart;
    }
}
