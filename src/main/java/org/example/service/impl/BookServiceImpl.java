package org.example.service.impl;

import lombok.Setter;
import org.example.context.ApplicationContext;
import org.example.entity.Book;
import org.example.entity.Message;
import org.example.entity.Page;
import org.example.reposytory.impl.BookRepositoryImpl;
import org.example.service.BookService;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Setter
public class BookServiceImpl implements BookService {
    private BookRepositoryImpl bookRepository = ApplicationContext.getInstance()
            .getObject(BookRepositoryImpl.class);
    private UserServiceImpl userService = ApplicationContext.getInstance()
            .getObject(UserServiceImpl.class);

    @Override
    public List<Optional<Book>> getPageOfBook(Page page) {
        return bookRepository.getBookList()
                .stream()
                .skip(page.getNumber() * page.getSize())
                .limit(page.getSize())
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Book> findByName(String name) {
        return bookRepository.getBookList()
                .stream()
                .filter(Optional::isPresent)
                .filter(e -> e.get().getName().contains(name))
                .findAny().orElse(Optional.empty());
    }

    @Override
    public boolean save(Book book) {
        if (bookRepository.getBookList()
                .contains(Optional.of(book))) {
            return false;
        }
        bookRepository.save(book);
        return true;
    }

    @Override
    public boolean remove(Book book) {
        if (!bookRepository.getBookList()
                .contains(Optional.of(book))) {
            return false;
        }
        bookRepository.remove(book);
        return true;
    }

    @Override
    public boolean update(Book book) {
        if (!bookRepository.getBookList()
                .stream()
                .filter(Optional::isPresent)
                .filter(e -> e.get().getName().equals(book.getName()))
                .findAny().orElse(Optional.empty())
                .isPresent()) {
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
