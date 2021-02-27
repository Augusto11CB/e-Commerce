package com.example.demo.controllers;

import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.repositories.ItemRepository;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.example.demo.controllers.ControllerECommerceUtils.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ItemControllerTest {

    private ItemController itemController;
    private ItemRepository itemRepository;

    private Item item;
    private List<Item> items;

    @Before
    public void Setup() {
        itemRepository = mock(ItemRepository.class);
        itemController = new ItemController(itemRepository);

        item = getDefaultItem();

        items = new ArrayList<>();
        items.add(item);
    }

    @Test
    public void getItemsWithSuccess() {

        when(itemRepository.findAll()).thenReturn(items);

        final ResponseEntity<List<Item>> responseEntity = itemController.getItems();
        final List<Item> returnedItems = responseEntity.getBody();

        assertNotNull(responseEntity);
        assertEquals(200, responseEntity.getStatusCodeValue());
        assertNotNull(returnedItems);
        assertArrayEquals(items.toArray(), returnedItems.toArray());
    }

    @Test
    public void getItemByIdWithSuccess() {

        when(itemRepository.findById(1L)).thenReturn(Optional.of(item));

        final ResponseEntity<Item> responseEntity = itemController.getItemById(1L);
        final Item ite = responseEntity.getBody();

        assertNotNull(responseEntity);
        assertEquals(200, responseEntity.getStatusCodeValue());
        assertNotNull(ite);
        assertEquals(DEFAULT_ITEM_NAME_TEST, ite.getName());
        assertEquals(DEFAULT_ITEM_PRICE_TEST, ite.getPrice());
        assertEquals(DEFAULT_ITEM_DESCRIPTION_TEST, ite.getDescription());

    }

    @Test
    public void getItemsByNameWithSuccess() {

        when(itemRepository.findByName(DEFAULT_ITEM_NAME_TEST)).thenReturn(items);

        final ResponseEntity<List<Item>> responseEntity = itemController.getItemsByName(DEFAULT_ITEM_NAME_TEST);
        final List<Item> itemsReturned = responseEntity.getBody();

        assertNotNull(responseEntity);
        assertEquals(200, responseEntity.getStatusCodeValue());
        assertNotNull(itemsReturned);
        assertArrayEquals(items.toArray(), itemsReturned.toArray());
    }
}