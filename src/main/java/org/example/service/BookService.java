package org.example.service;

import org.example.entity.Book;
import org.example.entity.Page;

import java.util.List;
import java.util.Optional;

public interface BookService {
    List<Optional<Book>> getPageOfBook(Page page);
    Optional<Book> findByName(String name);
    boolean save(Book book);
    boolean remove(Book book);
    boolean update(Book book);
}
