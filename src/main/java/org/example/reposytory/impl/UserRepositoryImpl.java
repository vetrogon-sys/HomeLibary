package org.example.reposytory.impl;

import lombok.Getter;
import lombok.Setter;
import org.example.context.ApplicationContext;
import org.example.dto.RegisterDto;
import org.example.entity.User;
import org.example.reposytory.UserRepository;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class UserRepositoryImpl implements UserRepository {
    private static final String USER_REPOSITORY_PATH = "src/main/resources/db/UserRepository.txt";
    @Getter
    @Setter
    private List<Optional<User>> userList = getAll();

    public static List<Optional<User>> getAll() {
        return ApplicationContext.getInstance()
                .getObject(FileLoader.class)
                .getObjectList(User.class, USER_REPOSITORY_PATH)
                .stream()
                .map(Optional::of)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<User> findByLogin(String login) {
        return userList.stream()
                .map(Optional::get)
                .filter(e -> e.getLogin().equals(login))
                .findAny();
    }

    @Override
    public void save(RegisterDto registerDto) {
        User user = User.builder()
                .login(registerDto.getLogin())
                .password(registerDto.getPassword())
                .role(registerDto.getRole())
                .build();

        userList.add(Optional.of(user));
        dbRefresh();
    }

    @Override
    public void update(User user) {
        List<Optional<User>> pastList = userList;
        userList = userList.stream()
                .filter(e -> !e.get().getLogin().equals(user.getLogin()))
                .collect(Collectors.toList());
        if (!userList.equals(pastList)) {
            userList.add(Optional.of(user));
            dbRefresh();
        }
    }

    private void dbRefresh() {
        try {
            FileOutputStream writeData = new FileOutputStream(USER_REPOSITORY_PATH);
            ObjectOutputStream writeStream = new ObjectOutputStream(writeData);

            List<User> list = userList.stream()
                    .filter(Optional::isPresent)
                    .map(Optional::get)
                    .collect(Collectors.toList());
            writeStream.writeObject(list);
            writeStream.flush();
            writeStream.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
