package org.example.reposytory;

import org.example.entity.Book;
import org.example.exception.CustomIOException;

import java.util.List;
import java.util.Optional;

public interface BookRepository {
    List<Optional<Book>> getAll() throws CustomIOException;
    void save(Book book) throws CustomIOException;
    void remove(Book book) throws CustomIOException;
}
