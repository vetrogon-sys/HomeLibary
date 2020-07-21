package org.example.entity;

import lombok.*;

import java.io.Serializable;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User implements Serializable {

    private String login;
    private String password;
    private Role role;
}
