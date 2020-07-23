package org.example.reposytory;

import org.example.dto.RegisterDto;
import org.example.entity.User;

import java.util.Optional;

public interface UserRepository {
    Optional<User> findByLogin(String login);
    void save(RegisterDto registerDto);
    void update(User user);
}
