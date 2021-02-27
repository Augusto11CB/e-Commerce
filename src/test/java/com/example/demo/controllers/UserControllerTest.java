package com.example.demo.controllers;

import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.CreateUserRequest;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

import static com.example.demo.controllers.ControllerECommerceUtils.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class UserControllerTest {

    private UserController userController;

    private UserRepository userRepository;
    private CartRepository cartRepository;
    private BCryptPasswordEncoder encoder;

    private User user;

    @Before
    public void setUp() {

        userRepository = mock(UserRepository.class);
        cartRepository = mock(CartRepository.class);
        encoder = mock(BCryptPasswordEncoder.class);

        userController = new UserController(userRepository, cartRepository, encoder);

        user = getDefaultUser();
    }

    @Test
    public void createUserWithSuccess() throws Exception {
        final CreateUserRequest r = new CreateUserRequest();
        r.setUsername("augusto2");
        r.setPassword("nice-password");
        r.setConfirmPassword("nice-password");

        when(encoder.encode("nice-password")).thenReturn("awesome-hashed-password");

        final ResponseEntity<User> responseEntity = userController.createUser(r);
        final User newUser = responseEntity.getBody();

        assertNotNull(responseEntity);
        assertEquals(200, responseEntity.getStatusCodeValue());

        assertNotNull(newUser);
        assertEquals(0, newUser.getId());
        assertEquals("augusto2", newUser.getUsername());
        assertEquals("awesome-hashed-password", newUser.getPassword());

    }

    @Test
    public void createUserWithPasswordNotMatchingCriteriaOfLength() {
        CreateUserRequest r = new CreateUserRequest();
        r.setUsername("augusto2");
        r.setPassword("123");
        r.setConfirmPassword("123");

        final ResponseEntity<User> responseEntity = userController.createUser(r);

        assertNotNull(responseEntity);
        assertNotEquals(200, responseEntity.getStatusCodeValue());
    }

    @Test
    public void findById() {

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        final ResponseEntity<User> responseEntity = userController.findById(1L);
        final User foundedUserById = responseEntity.getBody();

        assertNotNull(responseEntity);
        assertEquals(200, responseEntity.getStatusCodeValue());

        assertNotNull(foundedUserById);
        assertEquals(1L, foundedUserById.getId());
        assertEquals(DEFAULT_USER_NAME_TEST, foundedUserById.getUsername());
        assertEquals(DEFAULT_USER_PASSWORD_TEST, foundedUserById.getPassword());
    }

    @Test
    public void findByUsername() {

        when(userRepository.findByUsername(DEFAULT_USER_NAME_TEST)).thenReturn(user);

        final ResponseEntity<User> responseEntity = userController.findByUserName(DEFAULT_USER_NAME_TEST);
        User foundedUserByName = responseEntity.getBody();

        assertNotNull(responseEntity);
        assertEquals(200, responseEntity.getStatusCodeValue());

        assertNotNull(foundedUserByName);
        assertEquals(1L, foundedUserByName.getId());
        assertEquals(DEFAULT_USER_NAME_TEST, foundedUserByName.getUsername());
        assertEquals(DEFAULT_USER_PASSWORD_TEST, foundedUserByName.getPassword());
    }


}