package org.example.service.impl;

import org.example.entity.Book;
import org.example.entity.Message;
import org.example.entity.Page;
import org.example.exception.CustomIOException;
import org.example.reposytory.BookRepository;
import org.example.reposytory.impl.BookRepositoryImpl;
import org.example.service.BookService;
import org.example.service.UserService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class BookServiceImpl implements BookService {
    private BookRepository bookRepository = new BookRepositoryImpl();
    private UserService userService = new UserServiceImpl();

    @Override
    public List<Optional<Book>> getAll() throws CustomIOException {
        List<Optional<Book>> bookList;
        try {
            bookList = bookRepository.getAll();
        } catch (CustomIOException ignored) {
            bookList = new ArrayList<>();
        }
        return bookList;
    }

    @Override
    public List<Optional<Book>> getPageOfBook(Page page) throws CustomIOException {
        return getAll().stream()
                .skip(page.getNumber() * page.getSize())
                .limit(page.getSize())
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Book> findByPrefix(String prefix) throws CustomIOException {
        List<Optional<Book>>bookList = getAll();

        for (Optional<Book> book : bookList) {
            if (book.get().getName().contains(prefix)) {
                return book;
            }
        }
        return Optional.empty();
    }

    @Override
    public boolean save(Book book) throws CustomIOException {
        List<Optional<Book>> bookList = getAll();
        if (bookList.contains(Optional.of(book))) {
            return false;
        }
        bookRepository.save(book);
        return true;
    }

    @Override
    public boolean remove(Book book) throws CustomIOException {
        List<Optional<Book>> bookList = getAll();
        if (!bookList.contains(Optional.of(book))) {
            return false;
        }
        bookRepository.remove(book);
        return true;
    }

    @Override
    public boolean update(Book book) throws CustomIOException {
        List<Optional<Book>> bookList = getAll();
        if (!bookList.contains(Optional.of(book))) {
            return false;
        }
        bookRepository.update(book);
        Message message = Message.builder()
                .text("The book: " + book.getName() + " was updated")
                .build();
        userService.sendMessageToAll(message);
        return true;
    }
}
