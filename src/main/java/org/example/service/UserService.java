package org.example.service;

import org.example.dto.LoginDto;
import org.example.dto.RegisterDto;
import org.example.entity.Message;
import org.example.entity.User;
import org.example.exception.CustomIOException;
import org.example.exception.CustomWrongDataException;

import java.util.Optional;

public interface UserService {
    Optional<User> findByLogin(String login) throws CustomIOException;
    Optional<User> login(LoginDto loginDto) throws CustomWrongDataException, CustomIOException;
    Optional<User> save(RegisterDto registerDto) throws CustomIOException;
    Optional<User> update(User user) throws CustomIOException;
    void sendMessage(String login, Message message) throws CustomIOException;
    void sendMessageToAdmins(Message message) throws CustomIOException;
    void sendMessageToAll(Message message) throws CustomIOException;
}
