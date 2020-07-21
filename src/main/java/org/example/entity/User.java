package org.example.entity;

import lombok.*;

import java.util.HashMap;
import java.util.Map;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {

    private Map<String, String> loginDetails = new HashMap<>();
    private Role role;
    private boolean logged;
}
