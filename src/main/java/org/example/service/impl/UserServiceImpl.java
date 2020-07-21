package org.example.service.impl;

import lombok.Getter;
import lombok.Setter;
import org.example.dto.LoginDto;
import org.example.dto.RegisterDto;
import org.example.entity.Message;
import org.example.entity.Role;
import org.example.entity.User;
import org.example.exception.CustomIOException;
import org.example.exception.CustomWrongDataException;
import org.example.reposytory.UserRepository;
import org.example.reposytory.impl.UserRepositoryImpl;
import org.example.service.UserService;

import java.util.Base64;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
    public Optional<User> login(LoginDto loginDto) throws CustomWrongDataException, CustomIOException {
        Optional<User> user = findByLogin(loginDto.getLogin());
        if (user.isPresent()) {
            Base64.Decoder decoder = Base64.getDecoder();
            byte[] bytes = decoder.decode(user.get().getPassword());
            if ((new String(bytes)).equals(loginDto.getPassword())) {
                return user;
            } else {
                return Optional.empty();
            }
        }
        throw new CustomWrongDataException("User with same login doesn't exist");
    }

    @Override
    public Optional<User> save(RegisterDto registerDto) throws CustomIOException {
        if (findByLogin(registerDto.getLogin()).isPresent()) {
            return Optional.empty();
        }
        String encodedPassword = encoder.encodeToString(registerDto.getPassword().getBytes());
        registerDto.setPassword(encodedPassword);
        userRepository.save(registerDto);
        return findByLogin(registerDto.getLogin());
    }

    @Override
    public Optional<User> update(User user) throws CustomIOException {
        if (findByLogin(user.getLogin()).isPresent()) {
            return Optional.empty();
        }
        userRepository.update(user);
        return findByLogin(user.getLogin());
    }

    @Override
    public void sendMessage(String login, Message message) throws CustomIOException {
        User user = userRepository.findByLogin(login).orElse(null);
        if (user != null) {
            user.addMessage(message);
            update(user);
        }
    }

    @Override
    public void sendMessageToAdmins(Message message) throws CustomIOException {
        List<User> userList = userRepository.getAll().stream()
                .filter(Optional::isPresent)
                .map(Optional::get)
                .filter(e -> e.getRole().equals(Role.ADMIN))
                .collect(Collectors.toList());

        for (User user : userList) {
            user.addMessage(message);
            update(user);
        }
    }

    @Override
    public void sendMessageToAll(Message message) throws CustomIOException {
        List<User> userList = userRepository.getAll().stream()
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());

        for (User user : userList) {
            user.addMessage(message);
            update(user);
        }
    }
}
