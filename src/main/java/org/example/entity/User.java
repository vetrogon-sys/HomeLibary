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

    private List<Message> messageList = new ArrayList<>();

    public void addMessage(Message message) {
        messageList.add(message);
    }
}
