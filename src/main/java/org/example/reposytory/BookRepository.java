package org.example.reposytory;

import org.example.entity.Book;

public interface BookRepository {
    void save(Book book);
    void remove(Book book);
    void update(Book book);
}
