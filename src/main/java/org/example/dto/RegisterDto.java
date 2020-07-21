package org.example.dto;

import lombok.*;
import org.example.entity.Role;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegisterDto {
    private String login;
    private String password;
    private Role role;
}
