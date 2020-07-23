package org.example.entity;

import lombok.*;

import java.io.Serializable;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Message implements Serializable {
    private String text;
    private boolean viewed;
    private User sender;

    @Override
    public String toString() {
        String senderName;
        if (sender == null) {
            senderName = "System";
        } else {
            senderName = sender.getLogin();
        }
        return "Message{" +
                "text='" + text + '\'' +
                ", sender=" + senderName +
                '}';
    }
}
