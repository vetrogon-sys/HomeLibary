package org.example.reposytory.impl;

import org.example.dto.RegisterDto;
import org.example.entity.Role;
import org.example.entity.User;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.junit.Assert.*;

public class UserRepositoryImplTest {

    private UserRepositoryImpl userRepository;

    @Before
    public void setUp() throws Exception {
        userRepository = new UserRepositoryImpl();
        List<User> userList = Arrays.asList(
                new User("qwe", "password", Role.ADMIN),
                new User("asd", "password", Role.ADMIN),
                new User("zxc", "password", Role.USER),
                new User("rty", "password", Role.USER)
        );
        userRepository.setUserList(userList.stream()
                .map(Optional::of)
                .collect(Collectors.toList()));
    }

    @Test
    public void findByLogin_ExistUser_Test() {
        String expectedLogin = "qwe";
        Optional<User> optionalUser = userRepository.findByLogin(expectedLogin);

        assertTrue(optionalUser.isPresent());
        assertEquals(expectedLogin, optionalUser.get().getLogin());
        assertEquals("password", optionalUser.get().getPassword());
    }

    @Test
    public void findByLogin_NonExistUser_Test() {
        String expectedLogin = "login";
        Optional<User> optionalUser = userRepository.findByLogin(expectedLogin);

        assertFalse(optionalUser.isPresent());
    }

    @Test
    public void save() {
        userRepository.save(new RegisterDto("user", "password", Role.ADMIN));

        assertEquals(5, userRepository.getUserList().size());
        Optional<User> actualUser = userRepository.findByLogin("user");
        assertTrue(actualUser.isPresent());
        assertEquals("password", actualUser.get().getPassword());
        assertEquals(Role.ADMIN, actualUser.get().getRole());
    }

    @Test
    public void update_Exist_User_Test() {
        userRepository.update(new User("qwe", "qwe", Role.USER));

        Optional<User> actualUser = userRepository.findByLogin("qwe");
        assertTrue(actualUser.isPresent());
        assertEquals("qwe", actualUser.get().getPassword());
        assertEquals(Role.USER, actualUser.get().getRole());
    }

    @Test
    public void update_NonExist_User_Test() {
        userRepository.update(new User("user", "password", Role.ADMIN));

        Optional<User> actualUser = userRepository.findByLogin("user_123");
        assertFalse(actualUser.isPresent());
    }
}