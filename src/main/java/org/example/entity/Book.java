package org.example.entity;

import lombok.*;

import java.io.Serializable;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Book implements Serializable {
    private String name;
    private String description;

    @Override
    public String toString() {
        return "Book{" +
                "name='" + name + '\'' +
                "}\n";
    }
}
