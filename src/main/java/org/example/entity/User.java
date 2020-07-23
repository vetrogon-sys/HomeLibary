package org.example.entity;

import lombok.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User implements Serializable {
    private String login;
    private String password;
    private Role role;

    private List<Message> messageList;

    public User(String login, String password, Role role) {
        this.login = login;
        this.password = password;
        this.role = role;
    }

    public void addMessage(Message message) {
        if (messageList == null) {
            messageList = new ArrayList<>();
        }
        messageList.add(message);
    }

    @Override
    public String toString() {
        return "User{" +
                "login='" + login + '\'' +
                ", role=" + role +
                '}';
    }
}
