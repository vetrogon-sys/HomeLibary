package org.example.reposytory;

import org.example.dto.RegisterDto;
import org.example.exception.CustomIOException;
import org.example.entity.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository {
    List<Optional<User>> getAll() throws CustomIOException;
    Optional<User> findByLogin(String login) throws CustomIOException;
    void save(RegisterDto registerDto) throws CustomIOException;
    void update(User user) throws  CustomIOException;
}
