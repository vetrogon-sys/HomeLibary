package org.example.service.impl;

import lombok.Getter;
import lombok.Setter;
import org.example.dto.LoginDto;
import org.example.dto.RegisterDto;
import org.example.entity.User;
import org.example.exception.CustomIOException;
import org.example.exception.CustomWrongDataException;
import org.example.reposytory.UserRepository;
import org.example.reposytory.impl.UserRepositoryImpl;
import org.example.service.UserService;

import java.util.Base64;
import java.util.Optional;

@Setter
@Getter
public class UserServiceImpl implements UserService {
    private UserRepository userRepository = new UserRepositoryImpl();
    private final Base64.Encoder encoder = Base64.getEncoder();
    private final Base64.Decoder decoder = Base64.getDecoder();

    @Override
    public Optional<User> findByLogin(String login) throws CustomIOException {
        return userRepository.findByLogin(login);
    }

    @Override
    public boolean login(LoginDto loginDto) throws CustomWrongDataException, CustomIOException {
        Optional<User> user = findByLogin(loginDto.getLogin());
        if (user.isPresent()) {
            Base64.Decoder decoder = Base64.getDecoder();
            byte[] bytes = decoder.decode(user.get().getPassword());
            return (new String(bytes)).equals(loginDto.getPassword());
        }
        throw new CustomWrongDataException("User with same login doesn't exist");
    }

    @Override
    public boolean save(RegisterDto registerDto) throws CustomIOException {
        if (findByLogin(registerDto.getLogin()).isPresent()) {
            return false;
        }
        String encodedPassword = encoder.encodeToString(registerDto.getPassword().getBytes());
        registerDto.setPassword(encodedPassword);
        userRepository.save(registerDto);
        return true;
    }
}
