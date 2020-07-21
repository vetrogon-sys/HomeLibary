package org.example.service;

import org.example.dto.LoginDto;
import org.example.dto.RegisterDto;
import org.example.entity.User;
import org.example.exception.CustomIOException;
import org.example.exception.CustomWrongDataException;

import java.util.Optional;

public interface UserService {
    Optional<User> findByLogin(String login) throws CustomIOException;
    boolean login(LoginDto loginDto) throws CustomWrongDataException, CustomIOException;
    boolean save(RegisterDto registerDto) throws CustomIOException;
}
