package org.example.service;

import org.example.dto.LoginDto;
import org.example.dto.RegisterDto;
import org.example.entity.Message;
import org.example.entity.User;
import org.example.exception.CustomWrongDataException;

import java.util.Optional;

public interface UserService {
    Optional<User> findByLogin(String login);
    Optional<User> login(LoginDto loginDto) throws CustomWrongDataException;
    Optional<User> save(RegisterDto registerDto);
    Optional<User> update(User user);
    void sendMessage(String login, Message message);
    void sendMessageToAdmins(Message message);
    void sendMessageToAll(Message message);
}
