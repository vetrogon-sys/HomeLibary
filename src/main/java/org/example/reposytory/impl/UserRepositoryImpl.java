package org.example.reposytory.impl;

import org.example.dto.RegisterDto;
import org.example.entity.User;
import org.example.exception.CustomIOException;
import org.example.reposytory.UserRepository;

import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class UserRepositoryImpl implements UserRepository {
    private static final String USER_REPOSITORY_PATH = "src/main/resources/db/UserRepository.txt";

    @Override
    public List<Optional<User>> getAll() throws CustomIOException {
        try {
            InputStream isOb = new FileInputStream(USER_REPOSITORY_PATH);
            int size = isOb.available();

            byte[] userListBytes = new byte[size];
            for (int i = 0; i < size; i++) {
                userListBytes[i] = (byte) isOb.read();
            }
            if (userListBytes.length != 0) {
                ByteArrayInputStream bis = new ByteArrayInputStream(userListBytes);
                ObjectInputStream ois = new ObjectInputStream(bis);
                List<User> userList;

                try {
                    userList = (List<User>) ois.readObject();
                } catch (ClassNotFoundException e) {
                    throw new CustomIOException("Data in file does not fit");
                }

                return userList.stream()
                        .map(Optional::of)
                        .collect(Collectors.toList());
            } else {
                throw new CustomIOException("There is not some data");
            }
        } catch (IOException e) {
            throw new CustomIOException("Data read was failed");
        }
    }

    @Override
    public Optional<User> findByLogin(String login) throws CustomIOException {
        for (Optional<User> user : getAll()) {
            User current = user.orElse(null);
            if (current != null
                    && current.getLoginDetails().containsKey(login)) {
                return Optional.of(current);
            }
        }
        throw new CustomIOException("Something went's wrong. Can't read data");
    }

    @Override
    public void save(RegisterDto registerDto) {
        OutputStream osOb = null;
        Map<String, String> userDetails = new HashMap<>();
        userDetails.put(registerDto.getLogin(), registerDto.getPassword());
        User user = User.builder()
                .loginDetails(userDetails)
                .role(registerDto.getRole())
                .build();

        try {
            osOb = new FileOutputStream(USER_REPOSITORY_PATH);

            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(bos);
            oos.writeObject(user);
            byte[] userBytes = bos.toByteArray();

            osOb.write(userBytes);

            osOb.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (osOb != null) {
                try {
                    osOb.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
