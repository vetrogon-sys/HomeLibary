package org.example.service.impl;

import org.example.dto.LoginDto;
import org.example.dto.RegisterDto;
import org.example.entity.Message;
import org.example.entity.Role;
import org.example.entity.User;
import org.example.exception.CustomWrongDataException;
import org.example.reposytory.impl.UserRepositoryImpl;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.Base64;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class UserServiceImplTest {

    private UserRepositoryImpl userRepository = mock(UserRepositoryImpl.class);
    private UserServiceImpl userService;

    @Before
    public void setUp() throws Exception {
        userService = new UserServiceImpl();
        userService.setUserRepository(userRepository);
    }

    @Test
    public void login_ExistUser_Test() throws CustomWrongDataException {
        String decodePassword = Base64.getEncoder().encodeToString("qwe".getBytes());
        when(userRepository.findByLogin("qwe")).thenReturn(Optional.of(new User("qwe", decodePassword, Role.ADMIN)));

        assertTrue(userService.login(new LoginDto("qwe", "qwe")).isPresent());
    }

    @Test
    public void save_By_Exist_Username_Test() {
        String decodePassword = Base64.getEncoder().encodeToString("qwe".getBytes());
        when(userRepository.findByLogin("qwe")).thenReturn(Optional.of(new User("qwe", decodePassword, Role.ADMIN)));

        assertFalse(userService.save(new RegisterDto("qwe", "qwe", Role.ADMIN)).isPresent());
    }

    @Test
    public void update_By_NotExist_Username_Test() {
        String decodePassword = Base64.getEncoder().encodeToString("qwe".getBytes());
        when(userRepository.findByLogin("qwe")).thenReturn(Optional.of(new User("qwe", decodePassword, Role.ADMIN)));

        assertFalse(userService.update(new User("user", "qwe", Role.ADMIN)).isPresent());
    }

    @Test
    public void sendMessage_Test() {
        String decodePassword = Base64.getEncoder().encodeToString("qwe".getBytes());
        when(userRepository.findByLogin("qwe")).thenReturn(Optional.of(new User("qwe", decodePassword, Role.ADMIN)));

        Message message = new Message("message", false, new User("a", "a", Role.ADMIN));
        userService.sendMessage("qwe", message);
        assertTrue(userRepository.findByLogin("qwe").get().getMessageList().contains(message));
    }

    @Test
    public void sendMessageToAdmins_Test() {
        String decodePassword = Base64.getEncoder().encodeToString("qwe".getBytes());
        when(userRepository.getUserList()).thenReturn(Arrays.asList(
                Optional.of(new User("qwe", decodePassword, Role.ADMIN)),
                Optional.of(new User("asd", decodePassword, Role.ADMIN)),
                Optional.of(new User("zxc", decodePassword, Role.USER))
        ));

        Message message = new Message("message", false, new User("a", "a", Role.ADMIN));
        userService.sendMessageToAdmins(message);
        assertTrue(userRepository.getUserList().get(0).get().getMessageList().contains(message));
        assertTrue(userRepository.getUserList().get(1).get().getMessageList().contains(message));

        assertNull(userRepository.getUserList().get(2).get().getMessageList());
    }

    @Test
    public void sendMessageToAll_Test() {
        String decodePassword = Base64.getEncoder().encodeToString("qwe".getBytes());
        when(userRepository.getUserList()).thenReturn(Arrays.asList(
                Optional.of(new User("qwe", decodePassword, Role.ADMIN)),
                Optional.of(new User("asd", decodePassword, Role.ADMIN)),
                Optional.of(new User("zxc", decodePassword, Role.USER))
        ));

        Message message = new Message("message", false, new User("a", "a", Role.ADMIN));
        userService.sendMessageToAll(message);
        assertTrue(userRepository.getUserList().get(0).get().getMessageList().contains(message));
        assertTrue(userRepository.getUserList().get(1).get().getMessageList().contains(message));
        assertTrue(userRepository.getUserList().get(2).get().getMessageList().contains(message));
    }
}