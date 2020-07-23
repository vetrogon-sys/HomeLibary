package org.example.reposytory.impl;

import lombok.Getter;
import lombok.Setter;
import org.example.context.ApplicationContext;
import org.example.entity.Book;
import org.example.reposytory.BookRepository;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class BookRepositoryImpl implements BookRepository {
    private static final String BOOK_REPOSITORY_PATH = "src/main/resources/db/BookRepository.txt";
    @Getter
    @Setter
    private List<Optional<Book>> bookList = getAll();

    public static List<Optional<Book>> getAll() {
        return ApplicationContext.getInstance()
                .getObject(FileLoader.class)
                .getObjectList(Book.class, BOOK_REPOSITORY_PATH)
                .stream()
                .map(Optional::of)
                .collect(Collectors.toList());
    }

    @Override
    public void save(Book book) {
        bookList.add(Optional.ofNullable(book));
        dbRefresh();
    }

    @Override
    public void remove(Book book) {
        bookList.remove(Optional.ofNullable(book));
        dbRefresh();
    }

    @Override
    public void update(Book book) {
        bookList = bookList.stream()
                .filter(e -> !e.get().getName()
                        .equals(book.getName()))
                .collect(Collectors.toList());
        bookList.add(Optional.ofNullable(book));
        dbRefresh();
    }

    private void dbRefresh() {
        try {
            FileOutputStream writeData = new FileOutputStream(BOOK_REPOSITORY_PATH);
            ObjectOutputStream writeStream = new ObjectOutputStream(writeData);

            List<Book> list = bookList.stream()
                    .filter(Optional::isPresent)
                    .map(Optional::get)
                    .collect(Collectors.toList());
            writeStream.writeObject(list);
            writeStream.flush();
            writeStream.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
