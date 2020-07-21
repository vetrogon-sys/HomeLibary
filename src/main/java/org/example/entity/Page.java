package org.example.entity;

import lombok.*;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Page {

    private long size;
    private long number;

    public void nextPage() {
        this.number++;
    }

    public void previousPage() {
        if (number > 0) {
            this.number--;
        }
    }
}
