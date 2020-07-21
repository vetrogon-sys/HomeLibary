package org.example.service;

import org.example.entity.Book;
import org.example.entity.Page;
import org.example.exception.CustomIOException;

import java.util.List;
import java.util.Optional;

public interface BookService {
    List<Optional<Book>> getAll() throws CustomIOException;
    List<Optional<Book>> getPageOfBook(Page page) throws CustomIOException;
    Optional<Book> findByPrefix(String prefix) throws  CustomIOException;
    boolean save(Book book) throws CustomIOException;
    boolean remove(Book book) throws CustomIOException;
    boolean update(Book book) throws CustomIOException;
}
