package org.example.reposytory.impl;

import org.example.dto.RegisterDto;
import org.example.entity.User;
import org.example.exception.CustomIOException;
import org.example.reposytory.UserRepository;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class UserRepositoryImpl implements UserRepository {
    private static final String USER_REPOSITORY_PATH = "src/main/resources/db/UserRepository.txt";

    @Override
    public List<Optional<User>> getAll() throws CustomIOException {
        try{
            FileInputStream readData = new FileInputStream(USER_REPOSITORY_PATH);
            ObjectInputStream readStream = new ObjectInputStream(readData);

            ArrayList<User> userArrayList = (ArrayList<User>) readStream.readObject();
            readStream.close();

            return userArrayList.stream()
                    .map(Optional::of)
                    .collect(Collectors.toList());
        }catch (Exception e) {
            throw new CustomIOException("Can't read data");
        }
    }

    @Override
    public Optional<User> findByLogin(String login) throws CustomIOException {
        try {
            List<Optional<User>> userList;
            try {
                userList = getAll();
            } catch (CustomIOException e) {
                userList = new ArrayList<>();
            }

            for (Optional<User> user : userList) {
                User current = user.orElse(null);
                if (current != null
                        && current.getLogin().equals(login)) {
                    return Optional.of(current);
                }
            }
            throw new CustomIOException("Something went's wrong. Can't read data");
        } catch (CustomIOException e) {
            return Optional.empty();
        }

    }

    @Override
    public void save(RegisterDto registerDto) throws CustomIOException {
        User user = User.builder()
                .login(registerDto.getLogin())
                .password(registerDto.getPassword())
                .role(registerDto.getRole())
                .build();
        List<User> userList;
        try {
            userList = getAll().stream()
                    .map(Optional::get)
                    .collect(Collectors.toList());
        } catch (CustomIOException ignored) {
            userList = new ArrayList<>();
        }

        userList.add(user);
        try{
            FileOutputStream writeData = new FileOutputStream(USER_REPOSITORY_PATH);
            ObjectOutputStream writeStream = new ObjectOutputStream(writeData);

            writeStream.writeObject(userList);
            writeStream.flush();
            writeStream.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
